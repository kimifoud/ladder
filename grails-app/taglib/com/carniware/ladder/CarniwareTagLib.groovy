package com.carniware.ladder

class CarniwareTagLib {

    static namespace = "cw"

    def ratingChangeBadge = { attrs, body ->
//        <span class="badge badge-success"><i class="icon icon-arrow-up"></i> 5</span>
//        <span class="badge badge-important"><i class="icon icon-arrow-down"></i> 29</span>
        Number change
        try {
            change = attrs?.ratingChange
        } catch (Exception e) {
            log.error("cw:ratingChangeBadge was called without valid 'ratingChange' param.")
        }
        if (change) {
            def badge = change > 0 ? 'success' : 'error'
            def direction = change > 0 ? 'up' : 'down'

            out << "<span class=\"badge badge-${badge}\"><i class=\"icon icon-arrow-${direction}\"></i> ${change.abs()}</span>"
        }
    }
}
