package org.carniware.ladder

class Player extends User {

    BigDecimal eloRating = new BigDecimal(1000)

    static hasMany = [matches: Match]
    static mappedBy = [matches: 'player1', matches: 'player2']

    static constraints = {
        eloRating(nullable: false)
        matches(nullable: true)
    }
}
