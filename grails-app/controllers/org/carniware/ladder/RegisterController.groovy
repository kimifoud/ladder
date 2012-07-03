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
        def player = new Player(params)
        player.enabled = true
        if (player.validate()) {
            player.save()
            def userRole = Role.findByAuthority('ROLE_USER')
            UserRole.create(player, userRole, true)
            flash.message = "Registration successful! You may now login."
            redirect(controller: 'login', action: 'auth', params: [username: player.username])
        } else {
            flash.message = "Error registering user..."
            return [profile: player]
        }
    }
}
