package com.bol.kalaha.core;

public class Game {

    public enum Status {
        ACTIVE,
        DRAW,
        P1_WIN,
        P2_WIN
    }

    public record Result(Status status, PlayerID next, Board board) {
    }

    private Board board;

    private Player player;

    private Status status;

    public static Game create(Board board) {
        Game game = new Game();
        game.board = board;
        game.player = board.getPlayers().player1();
        game.status = Status.ACTIVE;
        return game;
    }

    public Result move(PlayerID num, int house) {
        if (!player.id().equals(num)) {
            throw new IllegalStateException(String.format("Player %s cannot take their turn yet", num));
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
}
