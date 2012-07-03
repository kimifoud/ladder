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
    Date dateCreated
    Date lastUpdated

	static constraints = {
		username blank: false, unique: true, email: true, maxSize: 50
		password blank: false
        firstName blank: false, maxSize: 25
        lastName blank: false, maxSize: 35
	}

	static mapping = {
		password column: '`password`'
	}

    static transients = ['fullName']

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
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
        return firstName + " " + lastName
    }

    String getFullName() {
        toString()
    }
}
