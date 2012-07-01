package org.carniware.ladder

import grails.test.mixin.*

@TestFor(LadderController)
@Mock(Ladder)
class LadderControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/ladder/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.ladderInstanceList.size() == 0
        assert model.ladderInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.ladderInstance != null
    }

    void testSave() {
        controller.save()

        assert model.ladderInstance != null
        assert view == '/ladder/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/ladder/show/1'
        assert controller.flash.message != null
        assert Ladder.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/ladder/list'


        populateValidParams(params)
        def ladder = new Ladder(params)

        assert ladder.save() != null

        params.id = ladder.id

        def model = controller.show()

        assert model.ladderInstance == ladder
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/ladder/list'


        populateValidParams(params)
        def ladder = new Ladder(params)

        assert ladder.save() != null

        params.id = ladder.id

        def model = controller.edit()

        assert model.ladderInstance == ladder
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/ladder/list'

        response.reset()


        populateValidParams(params)
        def ladder = new Ladder(params)

        assert ladder.save() != null

        // test invalid parameters in update
        params.id = ladder.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/ladder/edit"
        assert model.ladderInstance != null

        ladder.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/ladder/show/$ladder.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        ladder.clearErrors()

        populateValidParams(params)
        params.id = ladder.id
        params.version = -1
        controller.update()

        assert view == "/ladder/edit"
        assert model.ladderInstance != null
        assert model.ladderInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/ladder/list'

        response.reset()

        populateValidParams(params)
        def ladder = new Ladder(params)

        assert ladder.save() != null
        assert Ladder.count() == 1

        params.id = ladder.id

        controller.delete()

        assert Ladder.count() == 0
        assert Ladder.get(ladder.id) == null
        assert response.redirectedUrl == '/ladder/list'
    }
}
