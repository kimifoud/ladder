package org.carniware.ladder

class LeaderboardHistory {
	
	Date dateCreated
	Integer matchesTotal
	Integer matchesOfficial
	Integer matchesFriendly
    static belongsTo = [ladder: Ladder]
    static hasMany = [playerHistories : PlayerHistory] 
	
	static constraints = {
	}
}
