package org.carniware.ladder

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_USER'])
class MatchController {

    def springSecurityService
    def ratingService

    static defaultAction = "myMatches"

    def myMatches() {
        def userId = springSecurityService.currentUser.properties["id"]
        def offset = params.offset?.isInteger() ? params.offset as int : 0;
        def max = params.max?.isInteger() ? params.max as int : 5;
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
            render g.select(name: 'winner', optionKey: 'id', optionValue: 'name', from: winners, id: 'winner')
        } else {
            render g.select(name: 'winner', from: ["Select opponent first..."], id: 'winner')
        }
    }

    def save() {
        def match = new Match(description: params.description)
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
        def player2 = null
        if (params.player2?.isLong()) {
            player2 = Player.get(params.player2)
        }
        match.player2 = player2
        match.player2rating = player2?.eloRating
        if (params.winner?.isLong()) {
            match.winner = Player.get(params.winner)
        }
        match.played = new Date()
        match.friendly = params.friendly ?: false
        match.validate()
        if (!match.save(flush: true)) {
            def opponents = Player.findAllByLadderAndUserNotEqual(ladder, user)
            flash.message = message(code: 'match.save.error.default', default: 'Error saving match.')
            render(view: "newMatch", model: [match: match, opponents: opponents])
            return
        }
        if (!match.friendly) {
            try {
                ratingService.calculateNewRatings(player1, player2, match)
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
}
