package org.carniware.ladder

import java.util.Date;

class PlayerHistory {
	
	Player player
	BigDecimal rating
	Date dateCreated
	
	static belongsTo = [lbh: LeaderboardHistory]
    static constraints = {
		
    }
}
