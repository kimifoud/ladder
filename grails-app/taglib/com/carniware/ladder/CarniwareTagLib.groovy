package com.carniware.ladder

import org.springframework.web.servlet.support.RequestContextUtils

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
        Boolean alwaysShowPageSizes = new Boolean(attrs.alwaysShowPageSizes?:false)
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
                writer << '<lli>'
                writer << remoteLink(linkTagAttrs.clone()) {
                    firststep.toString()
                }
                writer << '</li>'
                writer << '<li>..</li>'
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
                writer << '<li>..</li>'
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
            selectParams.offset=0
            String paramsStr = selectParams.collect {it.key + "=" + it.value}.join("&")
            paramsStr = '\'' + paramsStr + '&max=\' + this.value'
            linkTagAttrs.params = paramsStr
            Boolean isPageSizesMap = pageSizes instanceof Map

            writer << "<li>" + select(from: pageSizes, value: max, name: "max", onchange: "${remoteFunction(linkTagAttrs.clone())}" ,class: 'remotepagesizes',
                    optionKey: isPageSizesMap?'key':'', optionValue: isPageSizesMap?'value':'') + "</li>"
        }
        writer << '</ul>'
    }

}
