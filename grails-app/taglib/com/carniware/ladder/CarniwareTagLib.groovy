package com.carniware.ladder

class CarniwareTagLib {

    static namespace = "cw"

    def ratingChangeBadge = { attrs ->
//        <span class="badge badge-success"><i class="icon icon-arrow-up"></i> 5</span>
//        <span class="badge badge-important"><i class="icon icon-arrow-down"></i> 29</span>
        Number change
        Number rating
        try {
            change = attrs?.ratingChange
            rating = attrs?.rating
        } catch (Exception e) {
            log.error("cw:ratingChangeBadge was called without valid 'ratingChange' param.")
        }
        if (change) {
            def badge = change > 0 ? 'success' : 'error'
            def direction = change > 0 ? 'up' : 'down'

            out << "<span class=\"badge badge-${badge}\">"
            if (rating) {
                out << rating.intValue()
                out << " "
            }
            out << "<i class=\"icon icon-arrow-${direction}\"></i> ${change.abs().intValue()}</span>"
        }
    }

    def bd = { attrs ->
        BigDecimal number
        int scale = 0
        try {
            number = attrs?.bd
            scale = attrs?.scale
        } catch (Exception e) {
            log.error("cw:bd was called without valid 'bd' param")
        }
        if (number) {
            out << number.setScale(scale)
        }
    }

    def cutString = { attrs ->
        String str = attrs?.str
        int length = attrs?.l ? Integer.parseInt(attrs.l) : 0
        if (str.length() < length || length <= 0) {
            out << str
        } else {
            out << "<div title=\"${str}\">"
            out << str.substring(0, length)
            out << "...</div>"
        }
    }

}
