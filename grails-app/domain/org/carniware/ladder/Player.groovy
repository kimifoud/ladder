package org.carniware.ladder

import java.math.RoundingMode

class Player extends User {

    BigDecimal eloRating = new BigDecimal("1500")

    static hasMany = [matches: Match]
    static mappedBy = [matches: 'player1', matches: 'player2']

    static constraints = {
        eloRating(nullable: false, scale: 0)
        matches(nullable: true)
    }
}
