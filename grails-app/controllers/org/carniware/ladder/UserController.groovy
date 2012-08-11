package org.carniware.ladder

import grails.plugins.springsecurity.Secured
import grails.validation.Validateable

@Secured(['ROLE_USER'])
class UserController {

    def springSecurityService

    def profile(UserCommand cmd) {
        User user = springSecurityService.currentUser
        if (!request.post) {
            return [user: user]
        }
        if (cmd.hasErrors()) {
            flash.message = "Errors in input..."
            return [user: cmd]
        } else {
            user.properties = cmd
            if (user.save()) {
                flash.message = "Success!"
                return [user: user]
            } else {
                flash.message = "Errors in input..."
                return [user: cmd]
            }
        }
    }
}

@Validateable
class UserCommand {
    String username
    String password
    String passwordRepeat
    String firstName
    String lastName

    static constraints = {
        username blank: false, unique: true, email: true, maxSize: 50
        password blank: false
        passwordRepeat(blank: false, validator: { passwd2, uc ->
            return passwd2 == uc.password })
        firstName blank: false, maxSize: 25, matches: "[a-zA-ZÅÄÖÜåäöü' '-]+"
        lastName blank: false, maxSize: 35, matches: "[a-zA-ZÅÄÖÜåäöü' '-]+"
    }
}
