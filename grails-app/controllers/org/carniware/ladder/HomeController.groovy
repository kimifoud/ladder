package org.carniware.ladder

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_USER'])
class HomeController {

    def matchService

    def index() {
        session["lastShoutId"] = 0L // we want shouts to be refreshed
        [latestMatches: matchService.latestMatches(), news: News.list(max: 3)]
    }

}
