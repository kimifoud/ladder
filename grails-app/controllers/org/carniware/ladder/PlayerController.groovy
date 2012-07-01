package org.carniware.ladder

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_USER'])
class PlayerController {

    def index() {
    }

}
