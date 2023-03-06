package com.bol.kalaha.api.service

import com.bol.kalaha.api.dao.document.GameDocument
import com.bol.kalaha.api.dao.repository.GameRepository
import com.bol.kalaha.api.mapper.GameMapper
import com.bol.kalaha.api.model.exception.GameNotFoundException
import com.bol.kalaha.core.Game
import com.bol.kalaha.core.model.Board
import io.github.benas.randombeans.EnhancedRandomBuilder
import spock.lang.Specification

class GameServiceSpec extends Specification {
    GameRepository repository
    GameService service

    def random = EnhancedRandomBuilder.aNewEnhancedRandom()

    void setup() {
        repository = Mock(GameRepository)
        service = new GameServiceImpl(repository)
    }

    def "creates game"() {
        given:
        def game = Game.create(Board.create(6, 6))
        def document = GameMapper.INSTANCE.coreModelToDocument(game)
        def savedDocument = random.nextObject(GameDocument.class)

        def expected = GameMapper.INSTANCE.documentToDto(savedDocument)

        when:
        def actual = service.create()

        then:
        1 * repository.save(document) >> savedDocument

        actual == expected
    }

    def "retrieves game"() {
        given:
        def id = random.nextObject(String.class)
        def document = random.nextObject(GameDocument.class)

        def expected = GameMapper.INSTANCE.documentToDto(document)

        when:
        def actual = service.get(id)

        then:
        1 * repository.findById(id) >> Optional.of(document)

        actual == expected
    }

    def "cannot retrieve non-existent game"() {
        given:
        def id = random.nextObject(String.class)

        when:
        service.get(id)

        then:
        1 * repository.findById(id) >> Optional.empty()

        def exc = thrown(GameNotFoundException.class)
        exc.message == "Game not found by ID=$id"
    }

    def "deletes game"() {
        given:
        def id = random.nextObject(String.class)

        when:
        service.delete(id)

        then:
        1 * repository.existsById(id) >> true
        1 * repository.deleteById(id)
    }

    def "cannot delete non-existing game"() {
        given:
        def id = random.nextObject(String.class)

        when:
        service.delete(id)

        then:
        1 * repository.existsById(id) >> false

        def exc = thrown(GameNotFoundException.class)
        exc.message == "Game not found by ID=$id"
    }
}
