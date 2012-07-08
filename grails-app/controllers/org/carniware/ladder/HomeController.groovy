package org.carniware.ladder

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_USER'])
class HomeController {

    def matchService

    def index() {
        [latestMatches: matchService.latestMatches()]
    }

}
