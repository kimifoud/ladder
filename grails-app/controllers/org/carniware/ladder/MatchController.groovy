package org.carniware.ladder

import grails.plugins.springsecurity.Secured
import org.carniware.ladder.Match

@Secured(['ROLE_USER'])
class MatchController {

    def springSecurityService

    static defaultAction = "myMatches"

    def myMatches() {
        def userId = springSecurityService.currentUser.properties["id"]
        log.debug("Current user's id: " + userId)
        def matches = Match.withCriteria(max: 10, offset: params.offset ?: 0){
            or {
                eq("player1.id", userId)
                eq("player2.id", userId)
            }
        }
        [matches: matches]
    }

    def newMatch() {
        [match: new Match()]
    }
}
