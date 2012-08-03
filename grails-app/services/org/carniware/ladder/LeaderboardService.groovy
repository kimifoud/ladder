package org.carniware.ladder

class LeaderboardService {

    static transactional = false

    def takeSnapshot(Ladder ladder) {
        def players = Player.withCriteria {
            join "ladder"
            eq "ladder.id", ladder.id
            order("eloRating", "desc")
        }
        if (players) {
            LeaderboardHistory lbh = new LeaderboardHistory()
            lbh.ladder = ladder
            Map<Player, BigDecimal> map = new HashMap<Player, BigDecimal>()
            players.each {
                log.debug("Player " + it.id + " (" + it.eloRating + ")")
                map.put(it, it.eloRating)
            }
            log.debug("map: " + map)
            lbh.leaderboard = map
            if (!lbh.save()) {
                log.debug("Error saving snapshot of leaderboard.")
                log.debug(lbh.errors)
            }
        } else {
            log.debug("No players to take snapshots of.")
        }
    }
}
