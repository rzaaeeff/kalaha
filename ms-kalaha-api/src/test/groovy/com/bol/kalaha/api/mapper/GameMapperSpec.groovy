package com.bol.kalaha.api.mapper

import com.bol.kalaha.api.dao.document.GameDocument
import com.bol.kalaha.api.model.enums.GameStatus
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
    def "converts core model to document"() {
        given:
        def game = Game.create(Board.create())

        when:
        def document = GameMapper.INSTANCE.coreModelToDocument(game)

        then:
        document.houses == game.board.houses.seedCount
        document.stores == game.board.stores.seedCount

        document.activePlayerId == (game.activePlayer.id() == P1 ? PLAYER_1 : PLAYER_2)
        document.status == switch (game.status) {
            case Game.Status.ACTIVE: yield GameStatus.ONGOING
            case Game.Status.DRAW: yield GameStatus.DRAW
            case Game.Status.P1_WIN: yield GameStatus.PLAYER_1_WIN
            case Game.Status.P2_WIN: yield GameStatus.PLAYER_2_WIN
        }
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
        game.board.houses.seedCount == document.houses
        game.board.stores.seedCount == document.stores

        game.player.id() == (document.activePlayerId == PLAYER_1 ? P1 : P2)
        game.status == switch (document.status) {
            case GameStatus.ONGOING: yield Game.Status.ACTIVE
            case GameStatus.DRAW: yield Game.Status.DRAW
            case GameStatus.PLAYER_1_WIN: yield Game.Status.P1_WIN
            case GameStatus.PLAYER_2_WIN: yield Game.Status.P2_WIN
        }

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