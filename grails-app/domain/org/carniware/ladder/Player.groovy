package org.carniware.ladder

class Player extends User {

    BigDecimal eloRating = new BigDecimal("1500")

    Ladder ladder

    static belongsTo = [ladder: Ladder]
    static hasMany = [matches: Match, comments: Comment]
    static mappedBy = [matches: 'player1', matches: 'player2']

    static constraints = {
        eloRating(nullable: false, scale: 0)
        matches(nullable: true)
        ladder(nullable: true)
    }
}
