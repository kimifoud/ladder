package org.carniware.ladder

class News {

    String title
    String body
    User author
    Date dateCreated

    static belongsTo = [author: User]

    static constraints = {
        title(blank: false, maxSize: 40)
        body(blank: false, maxSize: 10000)
        author(nullable: false)
    }

    static mapping = {
        sort dateCreated: "desc"
    }
}
