package com.bol.kalaha.api.mapper

import com.bol.kalaha.api.dao.document.GameDocument
import com.bol.kalaha.core.Game
import com.bol.kalaha.core.model.Board
import io.github.benas.randombeans.EnhancedRandomBuilder
import spock.lang.Specification
import spock.lang.Unroll

import static com.bol.kalaha.api.model.enums.PlayerID.PLAYER_1
import static com.bol.kalaha.api.model.enums.PlayerID.PLAYER_2
import static com.bol.kalaha.core.model.PlayerID.P1
import static com.bol.kalaha.core.model.PlayerID.P2

class GameMapperSpec extends Specification {
    def random = EnhancedRandomBuilder.aNewEnhancedRandom()

    @Unroll
    def "converts core model to document"(id) {
        given:
        def game = Game.create(Board.create())

        when:
        def document = GameMapper.INSTANCE.coreModelToDocument(game, id)

        then:
        document.id == id
        document.houses == game.board.houses.seedCount
        document.stores == game.board.stores.seedCount

        document.activePlayerId == (game.activePlayer.id() == P1 ? PLAYER_1 : PLAYER_2)

        where:
        id << [null, UUID.randomUUID().toString()]
    }

    @Unroll
    def "converts document to core model"(playerId) {
        given:
        def document = random.nextObject(GameDocument.class)
        document.houses = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]
        document.stores = [0, 1]
        document.setActivePlayerId(playerId)

        when:
        def game = GameMapper.INSTANCE.documentToCoreModel(document)

        then:

        game.player.id() == (document.activePlayerId == PLAYER_1 ? P1 : P2)
        game.board.houses.seedCount == document.houses
        game.board.stores.seedCount == document.stores

        where:
        playerId << [PLAYER_1, PLAYER_2]
    }

    def "converts document to dto"() {
        given:
        def document = random.nextObject(GameDocument.class)

        when:
        def dto = GameMapper.INSTANCE.documentToDto(document)

        then:
        dto.id == document.id
        dto.houses == document.houses
        dto.stores == document.stores
        dto.activePlayerId == document.activePlayerId
        dto.createdAt == document.createdAt
    }
}