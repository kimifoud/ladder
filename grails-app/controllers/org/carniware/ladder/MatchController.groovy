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
            join "player1"
            join "player2"
            or {
                eq("player1.id", userId)
                eq("player2.id", userId)
            }
            maxResults(max)
            firstResult(offset)
            order("id", "desc")
        }
        def criteria2 = Match.createCriteria()
        def matchesCount = criteria2.count {
            or {
                eq("player1.id", userId)
                eq("player2.id", userId)
            }
        }
        [matches: matches, matchesTotal: matchesCount]
    }

    def newMatch() {
        def userId = springSecurityService.currentUser.properties["id"]
        def user = User.findById(userId)
        def ladder = Ladder.findById(1) // TODO current ladder in session or something
        def opponents = Player.findAllByLadderAndUserNotEqual(ladder, user)
        [match: new Match(params), opponents: opponents]
    }

    def ajaxGetWinnersSelect = {
        def userId = springSecurityService.currentUser.properties["id"]
        def currentPlayer, selectedOpponent
        if (userId) {
            def user = User.findById(userId)
            def ladder = Ladder.findById(1)
            currentPlayer = Player.findByLadderAndUser(ladder, user)
            currentPlayer?.matches = null
        }
        if (params?.id && params.id != 'null') {
            selectedOpponent = Player.findById(params.id)
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
        def match = new Match(played: params.played, description: params.description)
        def ladder = Ladder.findById(1) // TODO current ladder in session or something

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
        def player2 = Player.findById(params.player2)
        match.player2 = player2
        match.player2rating = player2.eloRating
        match.winner = Player.findById(params.winner)
        match.friendly = params.friendly ?: false
        match.validate()
        if (!match.save(flush: true)) {
            def opponents = Player.findAllByLadderAndUserNotEqual(ladder, user)
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
//        redirect(action: "show", id: match.id) // TODO: this
        redirect(action: "myMatches")

    }

    def show() {

    }
}
