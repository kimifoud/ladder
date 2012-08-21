class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:'public', action: 'index')
        "500"(view:'/error')
        "/reservation/toggle/$reservation?"(controller: 'reservationStatus', action: 'toggle')
    }
}
