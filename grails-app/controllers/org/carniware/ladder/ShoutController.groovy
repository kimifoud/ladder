package org.carniware.ladder

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_USER'])
class ShoutController {

    static allowedMethods = [save: "POST"]

    def springSecurityService

    def ajaxSave() {
        def shout = new Shout(params)
        shout.shouter = springSecurityService.currentUser
        if (session["lastShout"] && System.currentTimeMillis() - session["lastShout"] < 5000) {
            render('<div class="alert alert-error">Cannot shout more often than every 5 seconds...</div>')
            return
        }
        if (shout.save()) {
            session["lastShout"] = shout.dateCreated.time
            forward(action: 'ajaxFetchLatest')
        } else {
            render('<div class="alert alert-error">Error saving shout...</div>')
        }
    }

    def ajaxFetchLatest() {
        def shouts = Shout.list(max: 25)
        render(template: "latestShouts", model: [shouts: shouts])
    }
}
