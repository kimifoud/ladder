package org.carniware.ladder

class RatingService {

    enum Result{
        PLAYER1_WINS,
        PLAYER2_WINS,
        TIE
    }

    def getW = { boolean isPlayerOne, RatingService.Result result ->
        def res
        switch (result) {
            case Result.PLAYER1_WINS:
                res = isPlayerOne ? "1" : "0"
                break
            case Result.PLAYER2_WINS:
                res = isPlayerOne ? "0" : "1"
                break
            case Result.TIE:
                res = "0.5"
        }
        new BigDecimal(res)
    }

    def getWe = { BigDecimal dr ->
        new BigDecimal(1).divide(10.power(dr.negate().divide(400) + 1))
    }

    /**
     * Rn = Ro + (W - We)
     *
     * Rn is the new rating, Ro is the old (pre-match) rating.
     * W is the result of the game (1 for a win, 0.5 for a draw, and 0 for a loss).
     * We is the expected result (win expectancy), from the following formula:
     * We = 1 / (10(-dr/400) + 1)
     * dr equals the difference in ratings.
     *
     * @param winner
     * @param loser
     */
    def calculateNewRatings(Player player1, Player player2, RatingService.Result result) {
        def p1Difference;
        def p2Difference;
        def p1W = getW(true, result)
        def p2W = getW(false, result)
        def dr = player1.eloRating.subtract(player2.eloRating).abs()
        def we = getWe(dr);

        def p1newRating = player1.eloRating.add(p1Difference)
        def p2newRating = player2.eloRating.add(p2Difference)
        player1.eloRating = p1newRating
        player2.eloRating = p2newRating
        player1.save()
        player2.save()
    }
}
