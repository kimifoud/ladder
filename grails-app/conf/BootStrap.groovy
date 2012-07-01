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

        def kimi = Player.findByUsername('kimi@kapsi.fi') ?: new Player(username: 'kimi@kapsi.fi', enabled: true, password: 'salasana', firstName: 'Kim', lastName: 'Foudila').save(failOnError: true)

        if (!kimi.authorities.contains(userRole)) {
            UserRole.create(kimi, userRole, true)
        }
        if (!kimi.authorities.contains(adminRole)) {
            UserRole.create(kimi, adminRole, true)
        }

        def suvi = Player.findByUsername('suvilol@kapsi.fi') ?: new Player(username: 'suvilol@kapsi.fi', enabled: true, password: 'salasana', firstName: 'Suvi', lastName: 'Ahonen',).save(failOnError: true)
        if (!suvi.authorities.contains(userRole)) {
            UserRole.create(suvi, userRole, true)
        }

        assert User.count() == 2
        assert Player.count() == 2
        assert Role.count() == 2
        assert UserRole.count() == 3

        def ladder = Ladder.findByTitle('Testiladder') ?: new Ladder(title: 'Testiladder', description: 'Spörö spärä lölööl')
        def match1 = new Match(player1: kimi, player2: suvi, played: new Date(), winner: kimi, player1rating: new BigDecimal(1000), player2rating: new BigDecimal(1000), description: "Hyvä matsi.")
        def match2 = new Match(player1: kimi, player2: suvi, played: new Date(), winner: kimi, player1rating: new BigDecimal(1050), player2rating: new BigDecimal(950), description: "Eri hyvä..!")
        def match3 = new Match(player1: kimi, player2: suvi, played: new Date(), winner: kimi, player1rating: new BigDecimal(1050), player2rating: new BigDecimal(950), description: "Eri hyvä..!")
        def match4 = new Match(player1: kimi, player2: suvi, played: new Date(), winner: kimi, player1rating: new BigDecimal(1050), player2rating: new BigDecimal(950), description: "Eri hyvä..!")
        def match5 = new Match(player1: kimi, player2: suvi, played: new Date(), winner: kimi, player1rating: new BigDecimal(1050), player2rating: new BigDecimal(950), description: "Eri hyvä..!")
        def match6 = new Match(player1: kimi, player2: suvi, played: new Date(), winner: kimi, player1rating: new BigDecimal(1050), player2rating: new BigDecimal(950), description: "Eri hyvä..!")
        def match7 = new Match(player1: kimi, player2: suvi, played: new Date(), winner: kimi, player1rating: new BigDecimal(1050), player2rating: new BigDecimal(950), description: "Eri hyvä..!")
        def match8 = new Match(player1: kimi, player2: suvi, played: new Date(), winner: kimi, player1rating: new BigDecimal(1050), player2rating: new BigDecimal(950), description: "Eri hyvä..!")
        def match9 = new Match(player1: kimi, player2: suvi, played: new Date(), winner: kimi, player1rating: new BigDecimal(1050), player2rating: new BigDecimal(950), description: "Eri hyvä..!")
        ladder.addToMatches(match1)
        ladder.addToMatches(match2)
        ladder.addToMatches(match3)
        ladder.addToMatches(match4)
        ladder.addToMatches(match5)
        ladder.addToMatches(match6)
        ladder.addToMatches(match7)
        ladder.addToMatches(match8)
        ladder.addToMatches(match9)
        ladder.save(failOnError: true, flush: true)
        assert Ladder.count() == 1
        assert Match.count() == 9
    }
    def destroy = {
    }
}
