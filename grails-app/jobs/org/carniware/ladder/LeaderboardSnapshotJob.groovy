package org.carniware.ladder

class LeaderboardSnapshotJob {
	static transactional = false

	def leaderboardService

	static triggers = {
//        simple name: 'testSnapshot', repeatInterval: 15000l, startDelay: 10000
		cron name: 'leaderboardSnapshotCronTrigger', startDelay: 60000l, cronExpression: '0 30 23 * * ?'
	}

	def execute() {
		log.debug("Executing LeaderboardSnapshotJob")
		def ladders = Ladder.list()
		log.debug("Found " + ladders?.size() + " ladders to take snapshots of.")
		ladders?.each {
			log.debug("Taking snapshot of ladder: " + it.title + " (id: " + it.id + ")")
			leaderboardService.takeSnapshot(it)
		}
	}
}
