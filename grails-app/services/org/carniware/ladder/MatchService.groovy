package org.carniware.ladder

class MatchService {

    def latestMatches() {
        def matches = Match.withCriteria {
            maxResults(5)
            order("dateCreated", "desc")
        }
        [latestMatches: matches]
    }
}
