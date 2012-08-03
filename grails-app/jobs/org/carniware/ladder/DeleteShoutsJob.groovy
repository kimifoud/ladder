package org.carniware.ladder

class DeleteShoutsJob {
    static triggers = {
//      simple name: 'deleteShouts', repeatInterval: 15000l, startDelay: 10000
        cron name: 'deleteShoutsCronTrigger', startDelay: 60000l, cronExpression: '0 15 8 * * ?'
    }

    def execute() {
        log.debug("Executing DeleteShoutsJob")
        def deletableShouts = Shout.list(offset: 25)
        log.debug("Found " + deletableShouts?.size() + " shouts to delete.")
        deletableShouts?.each {
            log.debug("Deleting shout: " + it.shout)
            it.delete()
        }
    }
}
