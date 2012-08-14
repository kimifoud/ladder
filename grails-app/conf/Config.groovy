import org.apache.log4j.rolling.RollingFileAppender
import org.apache.log4j.rolling.TimeBasedRollingPolicy
// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }


grails.project.groupId = 'org.carniware.ladder' // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [html: ['text/html', 'application/xhtml+xml'],
        xml: ['text/xml', 'application/xml'],
        text: 'text/plain',
        js: 'text/javascript',
        rss: 'application/rss+xml',
        atom: 'application/atom+xml',
        css: 'text/css',
        csv: 'text/csv',
        all: '*/*',
        json: ['application/json', 'text/json'],
        form: 'application/x-www-form-urlencoded',
        multipartForm: 'multipart/form-data'
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart = false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password', 'j_password']

// enable query caching by default
grails.hibernate.cache.queries = true

grails {
    mail {
        jndiName = "java:comp/env/mail/mySession"
    }
}

// set per-environment serverURL stem for creating absolute links
environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        grails.serverURL = "http://ladder.lovebo.at"
    }
}

// log4j configuration
log4j = {
    def rollingFile = new RollingFileAppender(name: 'rollingFileAppender', layout: pattern(conversionPattern: "%d [%t] %-5p %c{2} %x - %m%n"))
    // Rolling policy where log filename is logs/ladder.log.
    // Rollover each day, compress and save in logs/backup directory.
    def rollingPolicy = new TimeBasedRollingPolicy(fileNamePattern: 'logs/backup/ladder.%d{yyyy-MM-dd}.gz', activeFileName: 'logs/ladder.log')
    rollingPolicy.activateOptions()
    rollingFile.setRollingPolicy rollingPolicy

    appenders {
        appender rollingFile
    }

    root {
        // Use our newly created appender.
        error 'rollingFileAppender'
    }

    error 'org.codehaus.groovy.grails.web.servlet',  //  controllers
            'org.codehaus.groovy.grails.web.pages', //  GSP
            'org.codehaus.groovy.grails.web.sitemesh', //  layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping', // URL mapping
            'org.codehaus.groovy.grails.commons', // core / classloading
            'org.codehaus.groovy.grails.plugins', // plugins
            'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
            'org.springframework',
            'org.hibernate',
            'net.sf.ehcache.hibernate'

    warn 'org.mortbay.log'
    debug 'grails.app.jobs',
    info 'grails.app.org.carniware.ladder'
}

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'org.carniware.ladder.User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'org.carniware.ladder.UserRole'
grails.plugins.springsecurity.authority.className = 'org.carniware.ladder.Role'
grails.plugins.springsecurity.successHandler.defaultTargetURL = '/home'

grails.plugins.twitterbootstrap.fixtaglib = true
grails.plugins.twitterbootstrap.defaultBundle = 'bundle_bootstrap'

grails.gorm.default.mapping = {
    version true
    autoTimestamp true
}
grails.plugins.springsecurity.rememberMe.persistent = true
grails.plugins.springsecurity.rememberMe.persistentToken.domainClassName = 'org.carniware.ladder.PersistentLogin'
grails.plugins.springsecurity.rememberMe.cookieName = 'ladder_remember_me'
grails.plugins.springsecurity.rememberMe.tokenValiditySeconds = 7776000
grails.plugins.springsecurity.rememberMe.key = 'justanotherladdertokenkey'

grails.app.context = "/"

