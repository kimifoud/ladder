package com.carniware.ladder

import org.springframework.web.servlet.support.RequestContextUtils
import org.apache.commons.lang.time.DateUtils

class CarniwareTagLib {

    static namespace = "cw"

    def ratingChangeBadge = { attrs ->
        Number change
        Number rating
        try {
            change = attrs?.ratingChange
            rating = attrs?.rating
        } catch (Exception e) {
            log.error("cw:ratingChangeBadge was called without valid 'ratingChange' param.")
        }
        if (change != null) {
            def badge = change > 0 ? 'badge-success' : change < 0 ? 'badge-important' : ''
            def direction = change > 0 ? 'up' : change < 0 ? 'down' : 'right'

            out << "<span class=\"badge ${badge}\">"
            if (rating) {
                out << rating.intValue()
                out << " "
            }
            out << "<i class=\"icon icon-arrow-${direction}\"></i> ${change.abs().intValue()}</span>"
        }
    }

    def ratingBadge = { attrs ->
        Number rating
        try {
            rating = attrs?.rating
        } catch (Exception e) {
            log.error("cw:ratingBadge was called without valid 'rating' param.")
        }
        if (rating) {
            out << "<span class=\"badge badge-info\">"
            out << rating.intValue()
            out << "</span>"
        }
    }

    def dateFromNow = { attrs ->
        def date = attrs.date
        def shortFormat = attrs.format?.equals("short")
        if (shortFormat && !DateUtils.isSameDay(date, new Date())) {
            out << g.formatDate(date: date, format: "E, dd.MM.")
            return
        }
        def niceDate = getNiceDate(date)
        out << niceDate
    }

    static String getNiceDate(Date date) {
        def now = new Date()
        def diff = Math.abs(now.time - date.time)
        final long second = 1000
        final long minute = second * 60
        final long hour = minute * 60
        final long day = hour * 24
        def niceTime = ""
        long calc = 0;
        calc = Math.floor(diff / day)
        if (calc) {
            niceTime += calc + " day" + (calc > 1 ? "s " : " ")
            diff %= day
        }
        calc = Math.floor(diff / hour)
        if (calc) {
            niceTime += calc + " hour" + (calc > 1 ? "s " : " ")
            diff %= hour
        }
        calc = Math.floor(diff / minute)
        if (calc) {
            niceTime += calc + " minute" + (calc > 1 ? "s " : " ")
            diff %= minute
        }
        if (!niceTime) {
            niceTime = "Right now"
        } else {
            niceTime += (date.time > now.time) ? " from now" : " ago"
        }
        return niceTime
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

    def remotePaginate = {attrs ->
        def writer = out

        if (attrs.total == null)
            throwTagError("Tag [remotePaginate] is missing required attribute [total]")

        if (attrs.update == null)
            throwTagError("Tag [remotePaginate] is missing required attribute [update]")

        if (!attrs.action)
            throwTagError("Tag [remotePaginate] is missing required attribute [action]")

        def messageSource = grailsAttributes.getApplicationContext().getBean("messageSource")
        def locale = RequestContextUtils.getLocale(request)

        Integer total = attrs.total.toInteger()
        Integer offset = params.offset?.toInteger()
        Integer max = params.max?.toInteger()
        Integer maxsteps = params.maxsteps?.toInteger()
        def pageSizes = attrs.pageSizes ?: []
        Boolean alwaysShowPageSizes = new Boolean(attrs.alwaysShowPageSizes ?: false)
        Map linkTagAttrs = attrs


        if (!offset) offset = (attrs.offset ? attrs.offset.toInteger() : 0)

        if (!max) max = (attrs.max ? attrs.max.toInteger() : 10)

        if (!maxsteps) maxsteps = (attrs.maxsteps ? attrs.maxsteps.toInteger() : 10)

        Map linkParams = [offset: offset - max, max: max]
        Map selectParams = [:]
        if (params.sort) linkParams.sort = params.sort
        if (params.order) linkParams.order = params.order
        if (attrs.params) {
            linkParams.putAll(attrs.params)
            selectParams.putAll(linkParams)
        }

        if (attrs.id != null) {linkTagAttrs.id = attrs.id}
        linkTagAttrs.params = linkParams

        // determine paging variables
        boolean steps = maxsteps > 0
        Integer currentstep = (offset / max) + 1
        Integer firststep = 1
        Integer laststep = Math.round(Math.ceil(total / max))

        writer << '<ul>'

        // display previous link when not on firststep
        if (currentstep > firststep) {
            linkTagAttrs.class = 'prevLink'
            linkParams.offset = offset - max
            writer << '<li>'
            writer << remoteLink(linkTagAttrs.clone()) {
                (attrs.prev ? attrs.prev : messageSource.getMessage('paginate.prev', null, messageSource.getMessage('default.paginate.prev', null, 'Previous', locale), locale))
            }
            writer << '</li>'
        }

        // display steps when steps are enabled and laststep is not firststep
        if (steps && laststep > firststep) {
            linkTagAttrs.class = 'step'

            // determine begin and endstep paging variables
            Integer beginstep = currentstep - Math.round(maxsteps / 2) + (maxsteps % 2)
            Integer endstep = currentstep + Math.round(maxsteps / 2) - 1

            if (beginstep < firststep) {
                beginstep = firststep
                endstep = maxsteps
            }
            if (endstep > laststep) {
                beginstep = laststep - maxsteps + 1
                if (beginstep < firststep) {
                    beginstep = firststep
                }
                endstep = laststep
            }

            // display firststep link when beginstep is not firststep
            if (beginstep > firststep) {
                linkParams.offset = 0
                writer << '<li>'
                writer << remoteLink(linkTagAttrs.clone()) {
                    firststep.toString()
                }
                writer << '</li>'
                writer << '<li class=\"disabled\"><a href=\"#\">..</a></li>'
            }

            // display paginate steps
            (beginstep..endstep).each {i ->
                if (currentstep == i) {
                    writer << "<li class=\"active\"><a href=\"#\">${i}</a></li>"
                } else {
                    linkParams.offset = (i - 1) * max
                    writer << '<li>'
                    writer << remoteLink(linkTagAttrs.clone()) {i.toString()}
                    writer << '</li>'
                }
            }

            // display laststep link when endstep is not laststep
            if (endstep < laststep) {
                writer << '<li class="disabled"><a href="#">..</a></li>'
                linkParams.offset = (laststep - 1) * max
                writer << '<li>'
                writer << remoteLink(linkTagAttrs.clone()) { laststep.toString() }
                writer << '</li>'
            }
        }
        // display next link when not on laststep
        if (currentstep < laststep) {
            linkTagAttrs.class = 'nextLink'
            linkParams.offset = offset + max
            writer << '<li>'
            writer << remoteLink(linkTagAttrs.clone()) {
                (attrs.next ? attrs.next : messageSource.getMessage('paginate.next', null, messageSource.getMessage('default.paginate.next', null, 'Next', locale), locale))
            }
            writer << '<li>'
        }

        if ((alwaysShowPageSizes || total > max) && pageSizes) {
            selectParams.remove("max")
            selectParams.offset = 0
            String paramsStr = selectParams.collect {it.key + "=" + it.value}.join("&")
            paramsStr = '\'' + paramsStr + '&max=\' + this.value'
            linkTagAttrs.params = paramsStr
            Boolean isPageSizesMap = pageSizes instanceof Map

            writer << "<li>" + select(from: pageSizes, value: max, name: "max", onchange: "${remoteFunction(linkTagAttrs.clone())}", class: 'remotepagesizes',
                    optionKey: isPageSizesMap ? 'key' : '', optionValue: isPageSizesMap ? 'value' : '') + "</li>"
        }
        writer << '</ul>'
    }

    def cancelButton = {
        out << '<a href="javascript:history.back();" class="btn">Cancel</a>'
    }

    def matchResultString = { attrs ->
        def friendly = attrs.friendly
        Number p1rc = attrs.p1rc
        def p1name = attrs.p1name
        Number p2rc = attrs.p2rc
        def p2name = attrs.p2name

        if (friendly) {
            out << 'Friendly match. No changes in ratings.'
        } else if (p1rc == 0 && p2rc == 0) {
            out << 'No changes in ratings.'
        } else {
            out << p1name
            def p1 = p1rc < 0 ? ' loses ' : ' gains '
            out << p1
            out << p1rc.abs().intValue() + ' rating. '

            out << p2name
            def p2 = p2rc < 0 ? ' loses ' : ' gains '
            out << p2
            out << p2rc.abs().intValue() + ' rating. '
        }
    }

    def reservationLabel = { attrs ->
        Boolean online = attrs?.online ?: false
        Boolean reserved = attrs?.reserved ?: false
        Integer queueSize = attrs?.queueSize ?: 0
        if (!online) {
            out << "<span class=\"label\">N/A</span>"
        } else if (!reserved) {
            out << "<span class=\"label label-success\">FREE</span>"
        } else if (reserved && (!queueSize || queueSize < 1)) {
            out << "<span class=\"label label-warning\">Reserved</span>"
        } else {
            out << "<span class=\"label label-important\">"
            out << queueSize
            out << " in queue</span>"
        }
    }
}
