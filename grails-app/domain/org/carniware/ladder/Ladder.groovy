package org.carniware.ladder

class Ladder {

    static hasMany = [matches: Match, players: Player]

    String title
    String description

    static constraints = {
        title(blank: false)
        description(blank: false)
    }

    String toString() {
        title
    }

}
