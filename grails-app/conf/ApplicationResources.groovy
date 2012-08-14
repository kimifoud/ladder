modules = {
    application {
        dependsOn: 'jquery'
        resource url: 'js/application.js'
        resource url: 'css/main.css'
        resource url: 'css/mobile.css'
    }

    login {
        resource url: 'css/login.css'
    }

    error {
        resource url: 'css/errors.css'
    }

//    'knockout' {
//        resource url:'js/knockout-2.1.0.js', disposition: 'head'
//        resource url:'js/knockout-2.1.0.mapping.js', disposition: 'head'
//    }
    'knockout-dev' {
        resource url:'js/knockout-2.1.0.debug.js', disposition: 'head'
        resource url:'js/knockout.mapping-2.1.0.debug.js', disposition: 'head'
    }
}