package org.carniware.ladder

import java.util.Date;

class PlayerHistory {
	
	Player player
	BigDecimal rating
	Integer rank
	BigDecimal winningPercent
	Integer matchesTotal
	Integer matchesOfficial
	Integer matchesFriendly
	Date dateCreated
	
	static belongsTo = [lbh: LeaderboardHistory]
    static constraints = {
		winningPercent(scale: 4)
    }
}
