grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve

    repositories {
        inherits true // Whether to inherit repository definitions from plugins
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        runtime 'mysql:mysql-connector-java:5.1.16'

        compile 'log4j:apache-log4j-extras:1.0'
    }

    plugins {
        runtime ":hibernate:$grailsVersion"
        runtime ":jquery:1.7.1"
        compile ":jquery-ui:1.8.15"
        runtime ":resources:1.2-RC1"
        runtime ":cached-resources:1.0"
        runtime ":zipped-resources:1.0"
        runtime ":yui-minify-resources:0.1.5"

        runtime ":spring-security-core:1.2.7.3"
        compile ":cache-headers:1.1.5"
        compile (":twitter-bootstrap:2.0.2.25") { excludes 'svn' }
        compile ":fields:1.1"
        compile ":remote-pagination:0.3"
        compile ":quartz:1.0-RC2"
//		runtime ":database-migration:1.1"
		compile ":google-visualization:0.5.4"

        build ":tomcat:$grailsVersion"
    }
}
