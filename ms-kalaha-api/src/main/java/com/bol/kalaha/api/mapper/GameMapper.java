package com.bol.kalaha.api.mapper;

import com.bol.kalaha.api.dao.document.GameDocument;
import com.bol.kalaha.api.model.dto.GameDto;
import com.bol.kalaha.core.Game;
import com.bol.kalaha.core.model.Board;
import com.bol.kalaha.core.model.Pit;

import static com.bol.kalaha.api.model.enums.PlayerID.PLAYER_1;
import static com.bol.kalaha.api.model.enums.PlayerID.PLAYER_2;
import static com.bol.kalaha.core.model.PlayerID.P1;

public class GameMapper {
    public static final GameMapper INSTANCE = new GameMapper();

    private GameMapper() {
    }

    public GameDocument coreModelToDocument(Game game, String id) {
        var houses = game.getBoard().getHouses()
                .stream()
                .map(Pit::getSeedCount)
                .toList();

        var stores = game.getBoard().getStores()
                .stream()
                .map(Pit::getSeedCount)
                .toList();

        var playerId = game.getActivePlayer().id() == P1 ? PLAYER_1 : PLAYER_2;

        return GameDocument.builder()
                .id(id)
                .houses(houses)
                .stores(stores)
                .activePlayerId(playerId)
                .build();
    }

    public Game documentToCoreModel(GameDocument gameDocument) {
        var board = Board.from(
                gameDocument.getHouses().subList(0, 6), gameDocument.getStores().get(0),
                gameDocument.getHouses().subList(6, 12), gameDocument.getStores().get(1)
        );

        var activePlayer = gameDocument.getActivePlayerId() == PLAYER_1 ?
                board.getPlayers().player1() : board.getPlayers().player2();

        return Game.create(board, activePlayer);
    }

    public GameDto documentToDto(GameDocument gameDocument) {
        return GameDto.builder()
                .id(gameDocument.getId())
                .houses(gameDocument.getHouses())
                .stores(gameDocument.getStores())
                .activePlayerId(gameDocument.getActivePlayerId())
                .createdAt(gameDocument.getCreatedAt())
                .build();
    }
}
