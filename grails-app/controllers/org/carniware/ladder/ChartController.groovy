package org.carniware.ladder

import grails.plugins.springsecurity.Secured
import grails.converters.JSON

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
        } else {
            return
        }
        final max2 = max + 50 - max.mod(50)
        final min2 = min - min.mod(50)

        while (min2 < max2) {
            String key = min2 + "-" + (min2 + 49)
            map.put(key, 0)
            min2 += 50
        }
        players.each {
            String key = getKeyForRating(it.eloRating.intValue())
            def value = map.get(key, 0)
            map.put(key, ++value)
        }
        def sorted = map.sort({ k1, k2 -> k1.substring(0, 4).toInteger() <=> k2.substring(0, 4).toInteger() } as Comparator)
        sorted.each {
            data.add([it.key, it.value])
        }
        def columns = [['string', 'x'], ['number', 'Players']]
        render template: "ratingDistributionChart", model: ["columns": columns,
                "data": data, "width": params.width]
    }

    String getKeyForRating(Integer rating) {
        rating = rating - rating.mod(50) // round downwards to nearest 50
        String key = rating + "-" + (rating + 49)
        return key
    }

    def renderLadderNetworkGraph() {
        def matches = Match.withCriteria() {
            join "ladder"
            eq("ladder.id", 1L) // TODO multiple ladders support
        }
        MatchConnectionHolder holder = new MatchConnectionHolder()
        Set<Player> players = new HashSet<Player>()
        // add all connections between two players to a temporary holder
        matches.each {
            holder.addMatchConnection(it.player1, it.player2)
            players.add(it.player1)
            players.add(it.player2)
        }
        // create json
        def jsonData = []
        players.each { player ->
            List<MatchConnection> playersConnections = holder.getMatchConnectionsByPlayer(player)
            holder.removeFromMatchConnections(playersConnections)
            def playerResult = [id: player.id, name: player.name]
            playerResult << [data: [$color: '#83548B', $type: 'circle', $dim: 10]]
            playerResult << [adjacencies:[]]
            playersConnections.each {
                playerResult.put('adjacencies', [nodeTo: it.getOtherPlayersId(player), nodeFrom: player.id])
            }
            jsonData << playerResult
        }
        render jsonData as JSON
    }
}

private class MatchConnectionHolder {
    ArrayList<MatchConnection> matchConnections = new ArrayList<MatchConnection>()

    void addMatchConnection(Player one, Player two) {
        MatchConnection mc = null
        for (it in matchConnections) {
            if ((it.player1 == one && it.player2 == two) || (it.player1 == two && it.player2 == one)) {
                mc = it
                break
            }
        }
        if (mc == null) {
            mc = new MatchConnection(player1: one, player2: two)
            matchConnections.add(mc)
        }
        mc.matchesTotal += mc.matchesTotal
    }

    List<MatchConnection> getMatchConnectionsByPlayer(Player player) {
        List<MatchConnection> ret = new ArrayList<MatchConnection>()
        matchConnections.each {
            if (it.player1 == player || it.player2 == player) {
                ret.add(it)
            }
        }
        ret
    }

    void removeFromMatchConnections(Collection<MatchConnection> toBeRemoved) {
        matchConnections.removeAll(toBeRemoved)
    }
}

private class MatchConnection {
    Player player1
    Player player2
    int matchesTotal

    Long getOtherPlayersId(Player player) {
        player1 == player ? player2.id : player1.id
    }
}
