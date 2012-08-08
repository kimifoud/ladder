package org.carniware.ladder

class LeaderboardService {

    def takeSnapshot(Ladder ladder) {
        def players = Player.withCriteria {
            join "ladder"
            eq "ladder.id", ladder.id
            order("eloRating", "desc")
        }
        if (players) {
            LeaderboardHistory lbh = new LeaderboardHistory()
            lbh.ladder = ladder
            players.each {
                log.debug("Player " + it.id + " (" + it.eloRating + ")")
                PlayerHistory ph = new PlayerHistory()
				ph.player = it
				ph.rating = it.eloRating
				lbh.addToPlayerHistories(ph)
            }
            if (!lbh.save()) {
                log.debug("Error saving snapshot of leaderboard.")
                log.debug(lbh.errors)
            }
        } else {
            log.debug("No players to take snapshots of.")
        }
    }
}
