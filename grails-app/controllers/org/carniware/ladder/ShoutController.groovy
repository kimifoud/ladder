package org.carniware.ladder

import grails.plugins.springsecurity.Secured
import grails.converters.JSON

@Secured(['ROLE_USER'])
class ShoutController {

    static allowedMethods = [ajaxSave: "POST"]

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
            def jsonShout = [id: shout.id, shouted: shout.dateCreated.format("HH:mm"), shouter: shout.shouter.fullName, shout: shout.shout]
            render jsonShout as JSON
        } else {
            def errorShout = [id: 0, shouted: new Date().format("HH:mm"), shouter: 'SYSTEM', shout: 'Error saving shout!']
            render errorShout as JSON
        }
    }

    def ajaxFetchShouts(Long id) {
        def results = []
        def shouts
        if (id < 1) {
            shouts = Shout.list(max: 25)
        } else {
            shouts = Shout.withCriteria {
                gt "id", id
            }
        }
        shouts.each {
            def shoutResult = [id: it.id, shouted: it.dateCreated.format("HH:mm"), shouter: it.shouter.fullName, shout: it.shout]
            results << shoutResult
        }
        render results as JSON
    }
}
