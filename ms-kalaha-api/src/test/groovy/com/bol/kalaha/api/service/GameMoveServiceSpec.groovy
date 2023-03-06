package com.bol.kalaha.api.service


import com.bol.kalaha.api.dao.repository.GameRepository
import com.bol.kalaha.api.mapper.GameMapper
import com.bol.kalaha.api.model.dto.GameMoveDto
import com.bol.kalaha.api.model.exception.GameNotFoundException
import com.bol.kalaha.core.Game
import com.bol.kalaha.core.model.Board
import io.github.benas.randombeans.EnhancedRandomBuilder
import spock.lang.Specification

import java.time.LocalDateTime

import static com.bol.kalaha.api.model.enums.PlayerID.PLAYER_1
import static com.bol.kalaha.core.model.PlayerID.P1

class GameMoveServiceSpec extends Specification {
    GameRepository repository
    GameMoveService service

    def random = EnhancedRandomBuilder.aNewEnhancedRandom()

    void setup() {
        repository = Mock(GameRepository)
        service = new GameMoveServiceImpl(repository)
    }

    def "creates move"() {
        given:
        def gameId = random.nextObject(String.class)

        def moveDto = GameMoveDto.builder()
                .playerId(PLAYER_1)
                .pitId(1)
                .build()

        def gameCore = Game.create(Board.create(6, 6))

        def gameDocument = GameMapper.INSTANCE.coreModelToDocument(gameCore)
        gameDocument.id = gameId
        gameDocument.createdAt = random.nextObject(LocalDateTime.class)

        gameCore.move(P1, moveDto.pitId)

        def newGameDocument = GameMapper.INSTANCE.mergeCoreModelIntoDocument(gameCore, gameDocument)

        def expected = GameMapper.INSTANCE.documentToDto(newGameDocument)

        when:
        def actual = service.create(gameId, moveDto)

        then:
        1 * repository.findById(gameId) >> Optional.of(gameDocument)
        1 * repository.save(newGameDocument) >> newGameDocument

        actual == expected
    }

    def "cannot create move for non-existing game"() {
        given:
        def gameId = random.nextObject(String.class)

        when:
        service.create(gameId, random.nextObject(GameMoveDto.class))

        then:
        1 * repository.findById(gameId) >> Optional.empty()

        def exc = thrown(GameNotFoundException.class)
        exc.message == "Game not found by ID=$gameId"
    }
}
