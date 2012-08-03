package org.carniware.ladder

class RegisterController {

    def springSecurityService

    def index() {
        if (springSecurityService.isLoggedIn()) {
            redirect(controller: 'home', action: 'index')
        }
        render(view: 'save')
    }

    def save() {
        def user = new User(params)
        user.enabled = true
        if (user.validate()) {
            user.save()
            def userRole = Role.findByAuthority('ROLE_USER')
            UserRole.create(user, userRole, true)
            Player.link(user, Ladder.findById(1)) // TODO registration to ladders somewhere else
            flash.message = "Registration successful! You may now login."
            redirect(controller: 'login', action: 'auth', params: [username: user.username])
        } else {
            flash.message = "Error registering user..."
            return [user: user]
        }
    }
}
