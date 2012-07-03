package org.carniware.ladder

import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured
import org.carniware.ladder.Ladder

@Secured(['ROLE_USER'])
class LadderController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "leaderboard")
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [ladderInstanceList: Ladder.list(params), ladderInstanceTotal: Ladder.count()]
    }

    @Secured(['ROLE_ADMIN'])
    def create() {
        [ladderInstance: new Ladder(params)]
    }

    @Secured(['ROLE_ADMIN'])
    def save() {
        def ladderInstance = new Ladder(params)
        if (!ladderInstance.save(flush: true)) {
            render(view: "create", model: [ladderInstance: ladderInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'ladder.label', default: 'Ladder'), ladderInstance.id])
        redirect(action: "show", id: ladderInstance.id)
    }

    def show() {
        def ladderInstance = Ladder.get(params.id)
        if (!ladderInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'ladder.label', default: 'Ladder'), params.id])
            redirect(action: "list")
            return
        }

        [ladderInstance: ladderInstance]
    }

    @Secured(['ROLE_ADMIN'])
    def edit() {
        def ladderInstance = Ladder.get(params.id)
        if (!ladderInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'ladder.label', default: 'Ladder'), params.id])
            redirect(action: "list")
            return
        }

        [ladderInstance: ladderInstance]
    }

    @Secured(['ROLE_ADMIN'])
    def update() {
        def ladderInstance = Ladder.get(params.id)
        if (!ladderInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'ladder.label', default: 'Ladder'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (ladderInstance.version > version) {
                ladderInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'ladder.label', default: 'Ladder')] as Object[],
                        "Another user has updated this Ladder while you were editing")
                render(view: "edit", model: [ladderInstance: ladderInstance])
                return
            }
        }

        ladderInstance.properties = params

        if (!ladderInstance.save(flush: true)) {
            render(view: "edit", model: [ladderInstance: ladderInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'ladder.label', default: 'Ladder'), ladderInstance.id])
        redirect(action: "show", id: ladderInstance.id)
    }

    @Secured(['ROLE_ADMIN'])
    def delete() {
        def ladderInstance = Ladder.get(params.id)
        if (!ladderInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'ladder.label', default: 'Ladder'), params.id])
            redirect(action: "list")
            return
        }

        try {
            ladderInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'ladder.label', default: 'Ladder'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'ladder.label', default: 'Ladder'), params.id])
            redirect(action: "show", id: params.id)
        }
    }

    def leaderboard() {
        Long ladderId = 1L // TODO: multiple ladders support
        def ladder = Ladder.findById(ladderId)
        def players = Player.withCriteria {
            join "ladder"
            eq "ladder.id", ladderId
            order("eloRating", "desc")
        }
        def matches = Match.withCriteria {
            join "ladder"
            eq "ladder.id", ladderId
            order("id", "desc")
        }
        render(view: "ladder", model: [ladder:  ladder, players: players, matches: matches])
    }
}
