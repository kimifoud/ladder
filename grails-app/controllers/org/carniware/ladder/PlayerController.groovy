package org.carniware.ladder

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_USER'])
class PlayerController {

    static defaultAction = "show"

    def show() {
        def player
        def matches
        def matchesCount
        def offset = params.offset?.isInteger() ? params.offset as int : 0;
        def max = params.max?.isInteger() ? params.max as int : 5;
        if (params.id?.isNumber()) {
            player = Player.findById(params.id)
            matches = Match.withCriteria() {
                or {
                    eq("player1.id", params.id as Long)
                    eq("player2.id", params.id as Long)
                }
                order("id", "desc")
				maxResults(max)
            	firstResult(offset)
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

    def ajaxFetchMatches() {
        params.max = params.max ? Math.min(params.max as int, 50) : 10
        def matches = Match.createCriteria().list(max: params.max, offset: params.offset) {
            or {
                eq("player1.id", params.id as Long)
                eq("player2.id", params.id as Long)
            }
            order("id", "desc")
        }
        log.debug("matches.size(): " + matches.size())
        render(template: "/player/matches", model: [matches: matches, matchesTotal: matches.totalCount])
    }

}
