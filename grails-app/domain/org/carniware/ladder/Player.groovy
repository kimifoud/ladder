package org.carniware.ladder

import org.apache.commons.lang.builder.HashCodeBuilder

class Player {

    BigDecimal eloRating = new BigDecimal("1500")

    Ladder ladder
    User user

    static hasMany = [matches: Match]
    static mappedBy = [matches: 'player1', matches: 'player2']

    String getName() { user.fullName }

    static transients = ['name']

    static constraints = {
        eloRating(nullable: false, scale: 0)
        matches(nullable: true)
        ladder(nullable: true)
    }

    boolean equals(other) {
        if (!(other instanceof Player)) {
            return false
        }

        other.user?.id == user?.id &&
                other.role?.id == ladder?.id
    }

    int hashCode() {
        def builder = new HashCodeBuilder()
        if (user) builder.append(user.id)
        if (ladder) builder.append(ladder.id)
        builder.toHashCode()
    }

    static Player link(User user, Ladder ladder) {
        def p = Player.findByUserAndLadder(user, ladder)
        if (!p) {
            p = new Player()
            ladder?.addToPlayers(p)
            user?.addToPlayers(p)
            p.save()
        }
        return p
    }

    static void unlink(User user, Ladder ladder) {
        def p = Player.findByUserAndLadder(user, ladder)
        if (p) {
            ladder?.removeFromPlayers(p)
            user?.removeFromPlayers(p)
            p.delete()
        }
    }

    static void removeAll(User user) {
        executeUpdate 'DELETE FROM Player WHERE user=:user', [user: user]
    }

    static void removeAll(Ladder ladder) {
        executeUpdate 'DELETE FROM Player WHERE ladder=:ladder', [ladder: ladder]
    }

    String toString() {
        user.fullName
    }

}
