package org.carniware.ladder

class Match {

    Ladder ladder
    Player player1
    BigDecimal player1rating
    Player player2
    BigDecimal player2rating
    Player winner
    Date played
    String description

    static belongsTo = [ladder: Ladder]

    static constraints = {
        ladder(blank: false)
        player1(blank: false)
        player2(blank: false, validator: {val, obj ->
            obj.player1 != val
        })
        winner(blank: false, validator: { val, obj ->
            obj.player1 == val || obj.player2 == val
        })
        played(blank: false, max: new Date()+1, min: new Date()-7)
        description(blank: true)
    }

    static mapping = {
        player1 fetch: 'join'
        player2 fetch: 'join'
        winner fetch: 'join'
    }
}
