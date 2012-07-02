import org.carniware.ladder.Match
import org.carniware.ladder.User
import org.carniware.ladder.UserRole
import org.carniware.ladder.Role
import org.carniware.ladder.Player
import org.carniware.ladder.Ladder

class BootStrap {

    def init = { servletContext ->

        def userRole = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
        def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)

        def admin = Player.findByUsername('admin@admin.com') ?: new Player(username: 'admin@admin.com', enabled: true, password: 'pass', firstName: 'Ad', lastName: 'Min').save(failOnError: true)

        if (!admin.authorities.contains(userRole)) {
            UserRole.create(admin, userRole, true)
        }
        if (!admin.authorities.contains(adminRole)) {
            UserRole.create(admin, adminRole, true)
        }

        def user = Player.findByUsername('user@user.com') ?: new Player(username: 'user@user.com', enabled: true, password: 'pass', firstName: 'Test', lastName: 'User',).save(failOnError: true)
        if (!user.authorities.contains(userRole)) {
            UserRole.create(user, userRole, true)
        }

        assert User.count() == 2
        assert Player.count() == 2
        assert Role.count() == 2
        assert UserRole.count() == 3

        def ladder = Ladder.findByTitle('Testladder') ?: new Ladder(title: 'Testladder', description: 'This is a test ladder')
        ladder.save(failOnError: true, flush: true)
        assert Ladder.count() == 1
        assert Match.count() == 0
    }
    def destroy = {
    }
}
