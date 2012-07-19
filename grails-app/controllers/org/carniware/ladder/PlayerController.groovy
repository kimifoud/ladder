package org.carniware.ladder

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_USER'])
class PlayerController {

    def index() {
    }

    def show() {
        def player
        def matches
        def matchesCount
        if (params.id?.isNumber()) {
            player = Player.findById(params.id)
            matches = Match.withCriteria() {
                or {
                    eq("player1.id", params.id as Long)
                    eq("player2.id", params.id as Long)
                }
                order("id", "desc")
            }
            matchesCount = Match.createCriteria().count() {
                or {
                    eq("player1.id", params.id as Long)
                    eq("player2.id", params.id as Long)
                }
            }
        }
        [player: player, matches: matches, matchesTotal: matchesCount]
    }

}
