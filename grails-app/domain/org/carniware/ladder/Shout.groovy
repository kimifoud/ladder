package org.carniware.ladder

class Shout {

    User shouter
    String shout
    Date dateCreated

    static constraints = {
        shout(blank: false, maxSize: 256)
    }

    static mapping = {
        sort dateCreated: "desc"
    }
}
