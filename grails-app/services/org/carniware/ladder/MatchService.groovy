package org.carniware.ladder

class MatchService {

    List latestMatches() {
        def matches = Match.withCriteria {
            maxResults(5)
            order("dateCreated", "desc")
        }
        matches
    }
}
