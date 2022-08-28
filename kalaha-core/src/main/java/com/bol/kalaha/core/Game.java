package com.bol.kalaha.core;

import com.bol.kalaha.core.exception.IllegalMoveException;
import com.bol.kalaha.core.model.Board;
import com.bol.kalaha.core.model.Pit;
import com.bol.kalaha.core.model.Player;
import com.bol.kalaha.core.model.PlayerID;

public class Game {

    private Board board;
    private Player player;
    private Status status;

    public static Game create(Board board, Player activePlayer, Status status) {
        var game = create(board);
        game.player = activePlayer;
        game.status = status;
        return game;
    }

    public static Game create(Board board) {
        Game game = new Game();
        game.board = board;
        game.player = board.getPlayers().player1();
        game.status = Status.ACTIVE;
        return game;
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer() {
        return player;
    }

    public Status getStatus() {
        return status;
    }

    public Result move(PlayerID num, int house) {
        if (!player.id().equals(num)) {
            throw new IllegalMoveException(String.format("Player %s cannot take their turn yet", num));
        }

        Pit landed = player.turn(house);
        if (player.complete()) {
            otherPlayer().finish();
            status = declareWinner();
        }

        player = nextPlayer(landed);
        return new Result(status, player.id(), board);
    }

    private Status declareWinner() {
        Board.Players players = board.getPlayers();
        int score1 = players.player1().score();
        int score2 = players.player2().score();

        if (score1 > score2) {
            return Status.P1_WIN;
        }

        if (score2 > score1) {
            return Status.P2_WIN;
        }

        return Status.DRAW;
    }

    public Player nextPlayer(Pit landed) {
        if (landed.equals(player.store())) {
            return player;
        }

        return otherPlayer();
    }

    private Player otherPlayer() {
        Board.Players players = board.getPlayers();

        return switch (player.id()) {
            case P1 -> players.player2();
            case P2 -> players.player1();
        };
    }

    public Player getActivePlayer() {
        return player;
    }

    public enum Status {
        ACTIVE,
        DRAW,
        P1_WIN,
        P2_WIN
    }

    public record Result(Status status, PlayerID next, Board board) {
    }
}
