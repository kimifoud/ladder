package org.carniware.ladder

import java.math.RoundingMode

class RatingService {

    private static final BigDecimal K_WEIGHT_CONSTANT = new BigDecimal(30)
    private static final BigDecimal ONE = new BigDecimal(1)
    private static final BigDecimal TEN = new BigDecimal(10)
    private static final BigDecimal FOUR_HUNDRED = new BigDecimal(400)

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
        ONE.divide(TEN.power(dr.negate().divide(FOUR_HUNDRED, 3, RoundingMode.HALF_UP)) + ONE, 3, RoundingMode.HALF_UP)
    }

    /**
     * Rn = Ro + K * (W - We)
     *
     * Rn is the new rating, Ro is the old (pre-match) rating.
     * K is the weight factor constant.
     * W is the result of the game (1 for a win, 0.5 for a draw, and 0 for a loss).
     * We is the expected result (win expectancy), from the following formula:
     * We = 1 / (10(-dr/400) + 1)
     * dr equals the difference in ratings.
     *
     * @param player1
     * @param player2
     * @param match
     */
    def calculateNewRatings(Player player1, Player player2, Match match) {
        def result = player1 == match.winner ? RatingService.Result.PLAYER1_WINS : RatingService.Result.PLAYER2_WINS
        def p1W = getW(true, result)
        def p2W = getW(false, result)
        def dr = player1.eloRating.subtract(player2.eloRating)
        def p1we = getWe(dr);
        def p2we = getWe(dr.negate());
        def p1Difference = K_WEIGHT_CONSTANT.multiply(p1W.subtract(p1we)).setScale(0, RoundingMode.HALF_UP)
        def p2Difference = K_WEIGHT_CONSTANT.multiply(p2W.subtract(p2we)).setScale(0, RoundingMode.HALF_UP)

        player1.eloRating = player1.eloRating.add(p1Difference)
        player2.eloRating = player2.eloRating.add(p2Difference)
        match.setPlayer1ratingChange(p1Difference)
        match.setPlayer2ratingChange(p2Difference)
        player1.save()
        player2.save()
        match.save()
    }
}
