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
        def offset = params.offset?.isInteger() ? params.offset as int : 0;
        log.debug("offset: " + offset)
        def max = params.max?.isInteger() ? params.max as int : 5;
        log.debug("max: " + max)
        def criteria = Match.createCriteria()
        def matches = criteria.list{
            or {
                eq("player1.id", userId)
                eq("player2.id", userId)
            }
            maxResults(max)
            firstResult(offset)
            order("id", "desc")
        }
        def criteria2 = Match.createCriteria()
        def matchesCount = criteria2.count{
            or {
                eq("player1.id", userId)
                eq("player2.id", userId)
            }
        }
        [matches: matches, matchesTotal: matchesCount]
    }

    def newMatch() {
        [match: new Match()]
    }
}
