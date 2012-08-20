package org.carniware.ladder.rest

/**
 * RESTful controller for toggling game reservation indicator.
 *
 * Created with IntelliJ IDEA.
 * User: anttik
 * Date: 8/20/12
 * Time: 5:14 PM
 * To change this template use File | Settings | File Templates.
 */
class ReservationToggleController {

    def toggle() {
        def isReserved = Boolean.parseBoolean(params.reservation)
        //TODO add actual update to reservation indicator model
    }

}
