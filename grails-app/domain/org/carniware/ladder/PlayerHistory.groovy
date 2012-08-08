package org.carniware.ladder

class PlayerHistory {

	Player player
	BigDecimal rating
	
	static belongsTo = [lbh: LeaderboardHistory]
    static constraints = {
		
    }
}
