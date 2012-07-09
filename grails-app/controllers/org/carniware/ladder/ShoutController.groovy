package org.carniware.ladder

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_USER'])
class ShoutController {

    static allowedMethods = [save: "POST"]

    def springSecurityService

    def ajaxSave() {
        def shout = new Shout(params)
        shout.shouter = springSecurityService.currentUser
        def lastShout = Shout.findAllByShouter(shout.shouter, [max: 1])
        if (lastShout && System.currentTimeMillis() - lastShout.dateCreated.time < 5000) {
            render("<p>Cannot shout more often than every 5 seconds...</p>")
            return
        }
        if (shout.save()) {
            forward(action: 'ajaxFetchLatest')
        } else {
            render('<p>Error saving shout...</p>')
        }
    }

    def ajaxFetchLatest() {
        def shouts = Shout.list(max: 25)
        render(template: "latestShouts", model: [shouts: shouts])
    }
}
