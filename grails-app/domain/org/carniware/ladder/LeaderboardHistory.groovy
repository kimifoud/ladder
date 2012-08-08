package org.carniware.ladder

class LeaderboardHistory {
	
	Date dateCreated
    static belongsTo = [ladder: Ladder]
    static hasMany = [playerHistories : PlayerHistory] 
	
	static constraints = {
	}
}
