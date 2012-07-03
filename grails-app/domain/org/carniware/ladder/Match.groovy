package org.carniware.ladder

class Match {

    Ladder ladder
    Player player1
    BigDecimal player1rating
    BigDecimal player1ratingChange
    Player player2
    BigDecimal player2rating
    BigDecimal player2ratingChange
    Player winner
    Date played
    String description

    static belongsTo = [ladder: Ladder]
    static hasMany = [comments: Comment]

    static constraints = {
        ladder()
        player1()
        player2(validator: {val, obj ->
            obj.player1 != val
        })
        winner(validator: { val, obj ->
            obj.player1 == val || obj.player2 == val
        })
        played(max: new Date()+1, min: new Date()-7)
        description(blank: true, maxSize: 200)
        player1ratingChange(nullable: true)
        player2ratingChange(nullable: true)
    }

    static mapping = {
        player1 fetch: 'join'
        player2 fetch: 'join'
        winner fetch: 'join'
    }
}
