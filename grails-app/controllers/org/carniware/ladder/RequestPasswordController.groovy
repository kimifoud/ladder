package org.carniware.ladder

/**
 * Created with IntelliJ IDEA.
 * User: Antti
 * Date: 13.8.2012
 * Time: 18:34
 * To change this template use File | Settings | File Templates.
 */
class RequestPasswordController {

    def mailService

    def index() {
        render(view: 'request')
    }

    def requestPassword = {
        // send a one-time token to user's email for password reset
        sendPasswordRequestToken(params.email)

        render(view: 'requested')
        // Do not inform possible hacker if username does not exist!
    }

    def resetPassword = {
        def passwordRequestToken = params.token
        if (passwordRequestToken != null) {
            def user = User.findByPasswordRequestToken(passwordRequestToken)
            if (user != null) {
                sendNewRandomPassword(user.email)
            }
        }
        render(view: 'passwordsent')
        // Do not inform possible hacker if it failed!
    }

    def sendPasswordRequestToken(String email) {
        // check if user exists
        def user = User.findByEmail(email)
        if (user != null) {
            // generate request token and save for user
            user.passwordRequestToken = UUID.randomUUID()
            user.save()

            def mailBody = "Dear ${user.firstName},"
            mailBody = mailBody + "\r\rClick the following link to reset your password.\r\r"
            mailBody = mailBody + g.link(absolute:true, controller: 'requestPassword', action: 'resetPassword', params: [token: user.passwordRequestToken])

            mailService.sendMail {
                to email
                from "admin@ladder.lovebo.at"
                subject "Ladder password reset"
                body mailBody
            }
        }
    }

    def sendNewRandomPassword(String email) {
        // check if user exists
        def user = User.findByEmail(email)
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
                to email
                from "admin@ladder.lovebo.at"
                subject "Your new Ladder password"
                body mailBody
            }
        }
    }

}
