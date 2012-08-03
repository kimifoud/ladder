package org.carniware.ladder

import grails.converters.JSON

class LeaderboardHistory {

    static belongsTo = [ladder: Ladder]
    Date dateCreated
    Map<Player, BigDecimal> leaderboard // will not be persisted
    String leaderboardAsJSON

    static transients = ['leaderboard']

    static constraints = {
        leaderboardAsJSON(maxSize: 20000)
    }

    def onLoad() {
        leaderboard = JSON.parse(leaderboardAsJSON)
    }

    def beforeValidate() {
        leaderboardAsJSON = leaderboard as JSON
    }
}
