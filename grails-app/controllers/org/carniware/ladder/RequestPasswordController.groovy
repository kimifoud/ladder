package org.carniware.ladder

/**
 * Created with IntelliJ IDEA.
 * User: Antti
 * Date: 13.8.2012
 * Time: 18:34
 * To change this template use File | Settings | File Templates.
 */
class RequestPasswordController {

    def springSecurityService
    def mailService

    def index() {
        render(view: 'request')
    }

    def requestPassword = {
        // send a one-time token to user's email for password reset
        sendPasswordRequestToken(params.username)

        render(view: 'requested')
        // Do not inform possible hacker if username does not exist!
    }

    def resetPassword = {
        def passwordRequestToken = params.token
        if (passwordRequestToken != null) {
            def user = User.findByPasswordRequestToken(passwordRequestToken)
            if (user != null) {
                sendNewRandomPassword(user.username)
            }
        }
        render(view: 'passwordsent')
        // Do not inform possible hacker if it failed!
    }

    def sendPasswordRequestToken(String username) {
        // check if user exists
        def user = User.findByUsername(username)
        if (user != null) {
            // generate request token and save for user
            user.passwordRequestToken = UUID.randomUUID()
            user.save()

            def mailBody = "Dear ${user.firstName},"
            mailBody = mailBody + "\r\rClick the following link to reset your password."
            mailBody = mailBody + "\r\rhttp://ladder.lovebo.at/requestPassword/resetPassword?token=${user.passwordRequestToken}"

            mailService.sendMail {
                to username
                from "admin@ladder.lovebo.at"
                subject "Ladder password reset"
                body mailBody
            }
        }
    }

    def sendNewRandomPassword(String username) {
        // check if user exists
        def user = User.findByUsername(username)
        if (user != null) {
            // generate new password and send it through email
            def newPassword =  UUID.randomUUID().toString().substring(0, 8)
            user.password = newPassword
            user.passwordRequestToken = null
            user.save()

            def mailBody = "Dear ${user.firstName},"
            mailBody = mailBody + "\r\rThis is your new password for Ladder. Please change it as soon as possible."
            mailBody = mailBody + "\r\r${newPassword}"

            mailService.sendMail {
                to username
                from "admin@ladder.lovebo.at"
                subject "Your new Ladder password"
                body mailBody
            }
        }
    }

}
