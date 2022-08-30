package com.bol.kalaha.api.mapper;

import com.bol.kalaha.api.dao.document.GameDocument;
import com.bol.kalaha.api.model.dto.GameDto;
import com.bol.kalaha.api.model.enums.GameStatus;
import com.bol.kalaha.core.Game;
import com.bol.kalaha.core.Game.Status;
import com.bol.kalaha.core.model.Board;
import com.bol.kalaha.core.model.Pit;
import com.bol.kalaha.core.model.PlayerID;

import java.time.LocalDateTime;

import static com.bol.kalaha.api.model.enums.PlayerID.PLAYER_1;
import static com.bol.kalaha.api.model.enums.PlayerID.PLAYER_2;
import static com.bol.kalaha.core.model.PlayerID.P1;
import static com.bol.kalaha.core.model.PlayerID.P2;

public class GameMapper {
    public static final GameMapper INSTANCE = new GameMapper();

    private GameMapper() {
    }

    public GameDocument coreModelToDocument(Game game) {
        var houses = game.getBoard().getHouses()
                .stream()
                .map(Pit::getSeedCount)
                .toList();

        var stores = game.getBoard().getStores()
                .stream()
                .map(Pit::getSeedCount)
                .toList();

        var playerId = game.getActivePlayer().id() == P1 ? PLAYER_1 : PLAYER_2;

        var gameStatus = switch (game.getStatus()) {
            case ACTIVE -> GameStatus.ONGOING;
            case DRAW -> GameStatus.DRAW;
            case P1_WIN -> GameStatus.PLAYER_1_WIN;
            case P2_WIN -> GameStatus.PLAYER_2_WIN;
        };

        return GameDocument.builder()
                .houses(houses)
                .stores(stores)
                .activePlayerId(playerId)
                .status(gameStatus)
                .build();
    }

    public Game documentToCoreModel(GameDocument gameDocument) {
        var board = Board.from(
                gameDocument.getHouses().subList(0, 6), gameDocument.getStores().get(0),
                gameDocument.getHouses().subList(6, 12), gameDocument.getStores().get(1)
        );

        var activePlayer = gameDocument.getActivePlayerId() == PLAYER_1 ?
                board.getPlayers().player1() : board.getPlayers().player2();

        var status = switch (gameDocument.getStatus()) {
            case ONGOING -> Status.ACTIVE;
            case DRAW -> Status.DRAW;
            case PLAYER_1_WIN -> Status.P1_WIN;
            case PLAYER_2_WIN -> Status.P2_WIN;
        };

        return Game.create(board, activePlayer, status);
    }

    public GameDto documentToDto(GameDocument gameDocument) {
        return GameDto.builder()
                .id(gameDocument.getId())
                .houses(gameDocument.getHouses())
                .stores(gameDocument.getStores())
                .activePlayerId(gameDocument.getActivePlayerId())
                .createdAt(gameDocument.getCreatedAt())
                .status(gameDocument.getStatus())
                .build();
    }

    public GameDocument mergeCoreModelIntoDocument(Game gameCore, GameDocument gameDocument) {
        var result = coreModelToDocument(gameCore);

        result.setId(gameDocument.getId());
        result.setCreatedAt(gameDocument.getCreatedAt());
        result.setUpdatedAt(LocalDateTime.now());

        return result;
    }

    public PlayerID playerIdToCorePlayerId(com.bol.kalaha.api.model.enums.PlayerID playerID) {
        return playerID == PLAYER_1 ? P1 : P2;
    }
}
