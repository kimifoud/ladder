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
            def data = [error: 'Cannot shout more often than every 5 seconds...']
            render data as JSON
            return
        }
        if (shout.save()) {
            session["lastShout"] = shout.dateCreated.time
            session["lastShoutId"] = shout.id
            render(template: 'shout', bean: shout)
        } else {
            def data = [error: 'Could not save shout.']
            render data as JSON
            return
        }
    }

    def ajaxFetchShouts() {
        if (!session["lastShoutId"]) {
            session["lastShoutId"] = 0L
        }
        def lastId = session["lastShoutId"]
        def shouts
        if (lastId < 1) {
            shouts = Shout.list(max: 25)
        } else {
            shouts = Shout.withCriteria {
                gt "id", lastId
            }
        }
        if (shouts && !shouts.empty) {
            session["lastShoutId"] = shouts.first().id
        }
        render(template: 'shout', collection: shouts)
    }
}
