package org.carniware.ladder

class PublicController {

    def springSecurityService

    def index() {
        if (springSecurityService.isLoggedIn()) {
            redirect(controller: 'home', action: 'index')
        }
    }
}
