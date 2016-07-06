package rg.queue

import grails.converters.JSON

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class LinkController {

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)

        render Link.list(params) as JSON
    }

    def poll() {
        def link = Link.executeQuery('from Link where crawled = 0 and level < 5 order by rand()', [max: 1])

        if (link.size > 0)
            render link[0] as JSON
        else
            render(contentType: "application/json", status: 404) {
                response(status: 404, details: "No link left")
            }
    }

    def show(Link link) {
        if (link == null)
            render status: 404
        else
            render link as JSON
    }

    @Transactional
    def pop(Link link) {
        link.crawled = true

        if (link.save())
            render(contentType: "application/json", status: 200) {
                response(status: 200, details: "Pop successfully")
            }
        else
            render(contentType: "application/json", status: 400) {
                response(status: 400, details: "Failed to pop")
            }
    }

    @Transactional
    def create() {
        def link = new Link(params)

        if (!link.hasErrors() && link.save()) {
            render(contentType: "application/json", status: 201) {
                response(status:201, details: "Create link successfully")
            }
            return
        }

        render(contentType: "application/json", status: 400) {
            response(status: 400, details: "Failed to create new link")
        }
    }
}
