package org.carniware.ladder.rest

import org.carniware.ladder.LiveReservationStatus

/**
 * RESTful controller for toggling game reservation indicator.
 *
 * Created with IntelliJ IDEA.
 * User: anttik
 * Date: 8/20/12
 * Time: 5:14 PM
 * To change this template use File | Settings | File Templates.
 */
class ReservationStatusController {

    def toggle() {
        LiveReservationStatus status = LiveReservationStatus.get(1)
        def changed
        if (params.reserved) {
            status.reserved = params.boolean('reserved')
            changed = true
        }
//        if (params.queueSize) {
//            status.queueSize = params.int('queueSize')
//            changed = true
//        }
        if (changed) {
            status.save()
            render('ok')
        } else {
            render('nochange')
        }
    }

    def ajaxFetchStatus() {
        def status = LiveReservationStatus.get(1)
        // TODO online/offline
        render(template: 'reservationStatus', model: [online: status != null, reserved: status?.reserved, queueSize: status?.queueSize])
    }

}
