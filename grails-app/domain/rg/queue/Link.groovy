package rg.queue

class Link {

    Integer level = 0
    String url
    Boolean crawled = false
    Date dateCreated = new Date()
    Date lastUpdated = new Date()

    static constraints = {
        url blank: false, unique: true, nullable: false
        level nullable: false
        crawled nullable: false
    }
}
