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
        def opponents = Player.findAllByIdNotEqual(userId)
        [match: new Match(params), opponents: opponents]
    }

    def ajaxGetWinnersSelect = {
        def userId = springSecurityService.currentUser.properties["id"]
        def currentPlayer, selectedOpponent
        if (userId) {
            currentPlayer = Player.findById(userId)
            currentPlayer?.password = null
            currentPlayer?.matches = null
        }
        if (params?.id && params.id != 'null') {
            selectedOpponent = Player.findById(params.id)
            selectedOpponent.password = null
            selectedOpponent.matches = null
        }
        if (currentPlayer && selectedOpponent) {
            def winners = [currentPlayer, selectedOpponent]
            render g.select(name: 'winner', optionKey: 'id', optionValue: 'fullName', from: winners, id: 'winner')
        } else {
            render g.select(name: 'winner', optionKey: 'id', from: ["Select opponent first..."], id: 'winner')
        }
    }

    def save() {
        def match = new Match(params)
        def ladder = Ladder.findById(1) // TODO current ladder in session or something
        match.ladder = ladder
        def player1 = Player.findByUsername(springSecurityService.currentUser.properties["username"])
        if (!player1.ladder) {
            ladder.addToPlayers(player1)
        }
        match.player1 = player1
        match.player1rating = player1.eloRating
        def player2 = Player.findById(params.player2)
        if (!player2.ladder) {
            ladder.addToPlayers(player2)
        }
        match.player2 = player2
        match.player2rating = player2.eloRating
        match.winner = Player.findById(params.winner)
        match.validate()
        if (!match.save(flush: true)) {
            def userId = springSecurityService.currentUser.properties["id"]
            def opponents = Player.findAllByIdNotEqual(userId)
            render(view: "newMatch", model: [match: match, opponents: opponents])
            return
        }
        try {
            ratingService.calculateNewRatings(player1, player2, match)
        } catch (Exception e) {
            log.error("Error calculating ratings.", e)
            throw new RuntimeException("Crap happened while calculating new ratings.. :'(")
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'match.label', default: 'Match'), match.id])
//        redirect(action: "show", id: match.id) // TODO: this
        redirect(action: "myMatches")

    }

    def show() {

    }
}
