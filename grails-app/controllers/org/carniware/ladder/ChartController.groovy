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
}
