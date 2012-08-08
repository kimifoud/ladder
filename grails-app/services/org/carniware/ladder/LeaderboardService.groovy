package org.carniware.ladder

import java.math.RoundingMode;

import net.sf.ehcache.util.lang.VicariousThreadLocal;

import org.hibernate.FetchMode

class LeaderboardService {

    def takeSnapshot(Ladder ladder) {
        def players = Player.withCriteria {
            join "ladder"
            eq "ladder.id", ladder.id
            order("eloRating", "desc")
			fetchMode "matches", FetchMode.SELECT
        }
        if (players) {
			/* History entry for the ladder */
            LeaderboardHistory lbh = new LeaderboardHistory()
			def laddersMatches = Match.withCriteria {
				join "ladder"
				eq("ladder.id", ladder.id)
			}
			int total = 0;
			int officials = 0;
			int friendlies = 0;
			for (Match m : laddersMatches) {
				total++;
				if (!m.friendly) {
					officials++;
				} else {
					friendlies++;
				}
			}
            lbh.ladder = ladder
			lbh.matchesTotal = total;
			lbh.matchesOfficial = officials;
			lbh.matchesFriendly = friendlies;
			/* History entry for each player. */
			int rank = 1;
            players.each {
				def playerId = it.id
                log.debug("Player " + playerId + " (" + it.eloRating + ")")
                PlayerHistory ph = new PlayerHistory()
				ph.player = it
				ph.rating = it.eloRating
				ph.rank = rank++;
				int officialMatchCount = 0;
				int victories = 0;
				int friendlyMatchCount = 0;
				def matches = Match.withCriteria {
					join "player1"
					join "player2"
					or {
						eq "player1.id", playerId
						eq "player2.id", playerId
					}
				}
				for (Match m : matches) {
					if (!m.friendly) {
						officialMatchCount++;
						if (playerId.equals(m.winner.id)) {
							victories++;
						}
					} else {
						friendlyMatchCount++;
					}
				}
				ph.matchesTotal = matches.size()
				ph.matchesFriendly = friendlyMatchCount;
				ph.matchesOfficial = officialMatchCount;
				if (officialMatchCount != 0) {
					ph.winningPercent = new BigDecimal(victories).setScale(4).divide(new BigDecimal(officialMatchCount).setScale(4), RoundingMode.HALF_UP);
				} else {
					ph.winningPercent = 0;
				}
				log.debug("matchCount: " + officialMatchCount + ", victories: " + victories + ", winningPercent: " + ph.winningPercent)
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
