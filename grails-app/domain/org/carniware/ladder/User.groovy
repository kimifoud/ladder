package org.carniware.ladder

class User {

	transient springSecurityService

	String username
	String password
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

    String firstName
    String lastName
    String email
    Date dateCreated
    Date lastUpdated

    String passwordRequestToken

    static hasMany = [players: Player, shouts: Shout, news: News]

	static constraints = {
		username blank: false, maxSize: 50, unique: true
		password blank: false
        email blank: false, email: true, maxSize: 75, unique: true
        firstName blank: false, maxSize: 25, matches: "[a-zA-ZÅÄÖÜåäöü' '-]+"
        lastName blank: false, maxSize: 35, matches: "[a-zA-ZÅÄÖÜåäöü' '-]+"
        passwordRequestToken nullable: true
	}

	static mapping = {
		password column: '`password`'
	}

    static transients = ['fullName']

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

    Set<Ladder> getLadders() {
        Player.findAllByUser(this).collect { it.ladder } as Set
    }

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}

    String toString() {
        firstName + " " + lastName
    }

    String getFullName() {
        toString()
    }
}
