package org.carniware.ladder

import grails.plugins.springsecurity.Secured
import grails.converters.JSON

@Secured(['ROLE_USER'])
class MatchController {

    def springSecurityService
    def ratingService
	def matchService

    static defaultAction = "myMatches"

    def myMatches() {
        def userId = springSecurityService.currentUser.properties["id"]
        def offset = params.offset?.isInteger() ? params.offset as int : 0;
        def max = params.max?.isInteger() ? params.max as int : 10;
        def criteria = Match.createCriteria()
        def matches = criteria.list {
            or {
                player1 {
                    user {
                        eq("id", userId)
                    }
                }
                player2 {
                    user {
                        eq("id", userId)
                    }
                }
            }
            maxResults(max)
            firstResult(offset)
            order("id", "desc")
        }
        def criteria2 = Match.createCriteria()
        def matchesCount = criteria2.count {
            or {
                player1 {
                    user {
                        eq("id", userId)
                    }
                }
                player2 {
                    user {
                        eq("id", userId)
                    }
                }
            }
        }
        [matches: matches, matchesTotal: matchesCount]
    }

    def newMatch() {
        def userId = springSecurityService.currentUser.properties["id"]
        def user = User.get(userId)
        def ladder = Ladder.get(1) // TODO current ladder in session or something
        def opponents = Player.findAllByLadderAndUserNotEqual(ladder, user)
        [match: new Match(params), opponents: opponents]
    }

    def ajaxGetWinnersSelect = {
        def userId = springSecurityService.currentUser.properties["id"]
        def currentPlayer = null
        def selectedOpponent = null
        if (userId) {
            def user = User.get(userId)
            def ladder = Ladder.get(1)
            currentPlayer = Player.findByLadderAndUser(ladder, user)
            currentPlayer?.matches = null
        }
        if (params?.id && params.id != 'null') {
            selectedOpponent = Player.get(params.id)
            selectedOpponent.matches = null
        }
        if (currentPlayer && selectedOpponent) {
            def winners = [currentPlayer, selectedOpponent]
            render g.select(name: 'winner.id', optionKey: 'id', optionValue: 'name', from: winners, id: 'winner.id')
        } else {
            render g.select(name: 'winner.id', from: ["null": "Select opponent first..."], id: 'winner.id')
        }
    }

    def ajaxFindOpponents = {
        String term = params?.term
        def terms = term?.tokenize()
        if (!terms) {
           return
        }
        log.debug terms
        def opponentsFound = Player.withCriteria {
            user {
                ne("id", springSecurityService.currentUser.properties["id"])
                or {
                    terms.each {
                        log.debug it
                        ilike("firstName", it + "%")
                        ilike("lastName", it + "%")
	                }
                }
            }
            ladder {
                idEq(1L) // TODO support for multiple ladders
            }
            projections {
                property("id", "id")
                user {
                    property("firstName", "firstName")
                    property("lastName", "lastName")
                }
            }
        }
        def opponentsSelectList = []
        opponentsFound.each {
            def playerMap = [:] // jQuery autocomplete expects the JSON object to be with id/label/value.
            playerMap.put("id", it[0])
            playerMap.put("label", it[1] + " " + it[2])
            playerMap.put("value", it[1] + " " + it[2])
            opponentsSelectList.add(playerMap) // add to the arraylist
        }
        render (opponentsSelectList as JSON)
    }

    def save() {
        def match = new Match(params)
        def ladder = Ladder.get(1) // TODO current ladder in session or something

        def user = User.findByUsername(springSecurityService.currentUser.properties["username"])
        Player player1
        if (!user.ladders.contains(ladder)) {
            player1 = Player.link(user, ladder)
        } else {
            player1 = Player.findByUserAndLadder(user, ladder) // TODO use user.ladders relation instead of new fetch
        }

        match.ladder = ladder
        match.player1 = player1
        match.player1rating = player1.eloRating
        match.player2rating = match.player2?.eloRating
        match.played = new Date()
        match.validate()
        if (!match.save(flush: true)) {
            def opponents = Player.findAllByLadderAndUserNotEqual(ladder, user)
            flash.message = message(code: 'match.save.error.default', default: 'Error saving match.')
            render(view: "newMatch", model: [match: match, opponents: opponents])
            return
        }
        if (!match.friendly) {
            try {
                ratingService.calculateNewRatings(match.player1, match.player2, match)
            } catch (Exception e) {
                log.error("Error calculating ratings.", e)
                throw new RuntimeException("Crap happened while calculating new ratings.. :'(")
            }
        } else {
            match.player1ratingChange = new BigDecimal(0)
            match.player2ratingChange = new BigDecimal(0)
            match.save()
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'match.label', default: 'Match'), match.id])
        redirect(action: "show", id: match.id)

    }

    def show() {
        def match = Match.get(params.id)
        if (!match) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'match.label', default: 'Match'), params.id])
            redirect(action: "myMatches")
            return
        }
        [match: match, deletable: isMatchDeletable(match)]
    }

    private boolean isMatchDeletable(Match match) {
        def currentUser = springSecurityService.currentUser
        Calendar cal = Calendar.getInstance()
        cal.setTime(match.dateCreated)
        cal.add(Calendar.HOUR_OF_DAY, 1)
        def latestDeleteTime = cal.getTime()
        def deletable = (match.player1.user == currentUser || match.player2.user == currentUser) && new Date().before(latestDeleteTime)
        deletable
    }

    def remove() {
        def match = Match.get(params.id)
        if (!match) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'match.label', default: 'Match'), params.id])
            redirect(action: "myMatches")
            return
        }
        if (!isMatchDeletable(match)) {
            flash.message = message(code: 'match.not.deletable.message', default: 'This match cannot be deleted anymore. Deal with it.')
            redirect(action: "myMatches")
            return
        }
        match.player1.eloRating = match.player1.eloRating.subtract(match.player1ratingChange)
        match.player2.eloRating = match.player2.eloRating.subtract(match.player2ratingChange)

        match.delete()
        flash.message = message(code: 'match.deleted.message', default: 'Match deleted.')
        redirect(action: "myMatches")
    }
	
	def ajaxFetchLatestMatches() {
		render(template: "/home/latestMatches", model: [latestMatches: matchService.latestMatches()])
	}
}
