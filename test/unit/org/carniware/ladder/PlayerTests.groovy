package org.carniware.ladder



import grails.test.mixin.*
import org.carniware.ladder.Player

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Player)
class PlayerTests {

    void testPlayerConstraints() {
        def jami = new Player(username: 'jami@jami.net')
        mockForConstraintsTests(Player, [jami])

        def testPlayer = new Player()
        assertFalse(testPlayer.validate())

        testPlayer = new Player(username: 'jami@jami.net')
        assertFalse(testPlayer.validate())
        assertEquals("unique", testPlayer.errors["username"])
        assertEquals("nullable", testPlayer.errors["password"])
        assertEquals("nullable", testPlayer.errors["firstName"])
        assertEquals("nullable", testPlayer.errors["lastName"])
        testPlayer = new Player(username: 'jami@jami.net', password: 'spurd')
        assertFalse(testPlayer.validate())
        assertEquals("minSize", testPlayer.errors["password"])
        testPlayer = new Player(username: 'jussi@jami.net', password: 'spurdo', firstName: 'Jussi', lastName: 'Viljami')
        assertTrue(testPlayer.validate())
        testPlayer = new Player(username: 'jesse@jami.net', password: 'spurdo', firstName: 'Too long first name to test for constraint', lastName: 'Too long last name to test for constraint')
        assertFalse(testPlayer.validate())
        assertEquals("maxSize", testPlayer.errors["firstName"])
        assertEquals("maxSize", testPlayer.errors["lastName"])
    }
}
