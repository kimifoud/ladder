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

    infovis {
        resource url: 'js/jit.js'
        resource url: 'js/example1.js'
    }
}