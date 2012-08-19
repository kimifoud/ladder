package org.carniware.ladder

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_USER'])
class ChartController {

    def renderPlayerChart() {
        if (!params.id) {
            return
        }
        def playerHistories = PlayerHistory.withCriteria {
            eq("player.id", params.id as Long)
        }
        // data format: [['01.01.', 1500, 1], ['02.01.', 1470, 2], ...]
        def data = []
        playerHistories.each {
            data.add([it.dateCreated.format("dd.MM"), it.rating, it.rank])
        }
        log.debug(data)
        def columns = [['string', 'x'], ['number', 'Rating'], ['number', 'Rank']]
        render template: "playerChart", model: ["columns": columns,
                "data": data, "width": params.width]
    }

    def renderLadderRatingDistributionChart() {
        def ladderId
        if (!params.id && params.int('id') < 1) {
            ladderId = 1L // TODO multiple ladders support
        } else {
            ladderId = params.id as Long
        }
        def players = Player.withCriteria {
            join "ladder"
            eq "ladder.id", ladderId
            order("eloRating", "desc")
        }
        Map<String, Integer> map = new HashMap<String, Integer>()
        def data = []
        def min, max
        if (players && !players.empty) {
            min = players.last().eloRating.intValue()
            max = players.first().eloRating.intValue()
        }
        final max2 = max + 50 - max.mod(50)
        final min2 = min - min.mod(50)

        while (min2<max2) {
            String key = min2 + "-" + (min2+49)
            map.put(key,0)
            min2 += 50
        }
        players.each {
            String key = getKeyForRating(it.eloRating.intValue())
            def value = map.get(key, 0)
            map.put(key, ++value)
        }
        def sorted = map.sort({ k1, k2 -> k1.substring(0,4).toInteger() <=> k2.substring(0,4).toInteger() } as Comparator)
        sorted.each {
            data.add([it.key, it.value])
        }
        def columns = [['string', 'x'], ['number', 'Players']]
        render template: "ratingDistributionChart", model: ["columns": columns,
                "data": data, "width": params.width]
    }

    String getKeyForRating(Integer rating) {
        rating = rating - rating.mod(50) // round downwards to nearest 50
        String key = rating + "-" + (rating+49)
        return key
    }
}
