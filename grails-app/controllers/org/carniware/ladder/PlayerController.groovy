package org.carniware.ladder

import grails.plugins.springsecurity.Secured
import java.math.RoundingMode

@Secured(['ROLE_USER'])
class PlayerController {

    static defaultAction = "show"

    def show() {
        def player, matchesToList
        def offset = params.offset?.isInteger() ? params.offset as int : 0
        def officialMatchCount, friendlyMatchCount, victories, winningPercent, latest5
        def max = params.max?.isInteger() ? params.max as int : 5
        if (params.id?.isNumber()) {
            final playerId = params.id as Long
            player = Player.findById(playerId)
            matchesToList = Match.createCriteria().list(max: max, offset: offset) {
                or {
                    eq("player1.id", playerId)
                    eq("player2.id", playerId)
                }
                order("id", "desc")
            }

            def allMatches = Match.withCriteria {
                or {
                    eq("player1.id", playerId)
                    eq("player2.id", playerId)
                }
                order("id", "desc")
            }
            officialMatchCount = 0
            friendlyMatchCount = 0
            victories = 0
            StringBuilder sb = new StringBuilder()
            boolean win
            for (Match m : allMatches) {
                if (!m.friendly) {
                    officialMatchCount++
                    if (playerId.equals(m.winner.id)) {
                        win = true
                        victories++
                    } else {
                        win = false
                    }
                    if (officialMatchCount < 6) {
                        sb.append(win ? "W" : "L")
                        if (officialMatchCount < 5) {
                            sb.append("-")
                        }
                    }
                } else {
                    friendlyMatchCount++
                }
            }
            if (officialMatchCount != 0) {
                winningPercent = new BigDecimal(victories).setScale(4).divide(new BigDecimal(officialMatchCount).setScale(4), RoundingMode.HALF_UP)
            } else {
                winningPercent = 0
            }
            latest5 = sb.length() > 0 ? sb.toString() : "-"
        }
        def wp = winningPercent.multiply(new BigDecimal(100)).setScale(2).toString() + " %"
        [player: player, matches: matchesToList, matchesTotal: matchesToList.totalCount, matchesOfficial: officialMatchCount,
                matchesFriendly: friendlyMatchCount, victories: victories, winningPercent: wp, latest5: latest5]
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
