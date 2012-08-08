import org.carniware.ladder.Match
import org.carniware.ladder.User
import org.carniware.ladder.UserRole
import org.carniware.ladder.Role
import org.carniware.ladder.Player
import org.carniware.ladder.Ladder
import grails.util.GrailsUtil
import grails.util.Environment

class BootStrap {

    def init = { servletContext ->
        switch (Environment.current) {
            case Environment.DEVELOPMENT:
                def roleUser = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
                def roleAdmin = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)

                def admin = User.findByUsername('admin@admin.com') ?: new User(username: 'admin@admin.com', enabled: true, password: 'pass', firstName: 'Ad', lastName: 'Min').save(failOnError: true)

                if (!admin.authorities.contains(roleUser)) {
                    UserRole.create(admin, roleUser, true)
                }
                if (!admin.authorities.contains(roleAdmin)) {
                    UserRole.create(admin, roleAdmin, true)
                }

                def user = User.findByUsername('user@user.com') ?: new User(username: 'user@user.com', enabled: true, password: 'pass', firstName: 'Test', lastName: 'User',).save(failOnError: true)
                if (!user.authorities.contains(roleUser)) {
                    UserRole.create(user, roleUser, true)
                }

                def ladder = Ladder.findByTitle('Testladder') ?: new Ladder(title: 'Testladder', description: 'This is a test ladder').save(failOnError: true, flush: true)

				Player userPlayer, adminPlayer
                if (!user.ladders.contains(ladder)) {
                    userPlayer = Player.link(user, ladder)
                }
                if (!admin.ladders.contains(ladder)) {
                    adminPlayer = Player.link(admin, ladder)
                }

                assert User.count() == 2
                assert Role.count() == 2
                assert UserRole.count() == 3
                assert Ladder.count() == 1
                assert Match.count() == 0
                assert Player.count() == 2
				
				for (int i = 0; i < 100; i++) {
					Match match = new Match(ladder: ladder, player1: userPlayer, player2: adminPlayer, winner: userPlayer, player1rating: new BigDecimal(1500+i), player2rating: new BigDecimal(1500-i), player1ratingChange: new BigDecimal(1), player2ratingChange: new BigDecimal(-1), played: new Date(), description: "Test match...")
					match.save()
				}
				assert Match.count() == 100
                break;
            case Environment.TEST: break;
            case Environment.PRODUCTION: break;
            default: break;
        }
    }
    def destroy = {
    }
}
