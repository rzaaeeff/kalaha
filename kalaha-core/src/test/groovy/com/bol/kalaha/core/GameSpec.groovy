package com.bol.kalaha.core

import com.bol.kalaha.core.exception.IllegalMoveException
import com.bol.kalaha.core.model.Board
import spock.lang.Specification

import static com.bol.kalaha.core.Game.Status.ACTIVE
import static com.bol.kalaha.core.model.PlayerID.P1
import static com.bol.kalaha.core.model.PlayerID.P2

class GameSpec extends Specification {
    private Game game

    void setup() {
        game = Game.create(Board.create())
    }

    def "player one should take first turn"() {
        when:
        def player = game.activePlayer

        then:
        player.id() == P1
    }


    def "should reject move by inactive player"() {
        when:
        game.move(P2, 1)

        then:
        thrown(IllegalMoveException.class)
    }


    def "should allow player to sow"() {
        when:
        def result = game.move(P1, 1)

        then:
        result.status() == ACTIVE

        assertBoard(
                result.board(),
                [0, 5, 5, 5, 5, 4] as int[], 0,
                [4, 4, 4, 4, 4, 4] as int[], 0
        )
    }


    def "should allow alternate turns"() {
        when:
        def result = game.move(P1, 1)

        then:
        result.next() == P2
        result.status() == ACTIVE

        assertBoard(
                result.board(),
                [0, 5, 5, 5, 5, 4] as int[], 0,
                [4, 4, 4, 4, 4, 4] as int[], 0
        )

        when:
        result = game.move(P2, 1)

        then:
        result.next() == P1
        result.status() == ACTIVE

        assertBoard(
                result.board(),
                [0, 5, 5, 5, 5, 4] as int[], 0,
                [0, 5, 5, 5, 5, 4] as int[], 0
        )

    }


    def "should get another turn when landing in own store"() {
        when:
        def result = game.move(P1, 3)

        then:
        result.status() == ACTIVE
        result.next() == P1

        assertBoard(
                result.board(),
                [4, 4, 0, 5, 5, 5] as int[], 1,
                [4, 4, 4, 4, 4, 4] as int[], 0
        )
    }


    def "should capture opposite when landing in own empty house"() {
        given:
        def board = Board.from(
                [4, 4, 4, 4, 0, 5], 1,
                [5, 5, 4, 4, 4, 4], 0
        )

        when:
        def game = Game.create(board)

        then:
        assertBoard(
                game.board,
                [4, 4, 4, 4, 0, 5] as int[], 1,
                [5, 5, 4, 4, 4, 4] as int[], 0
        )

        when:
        def result = game.move(P1, 1)

        then:
        assertBoard(
                result.board(),
                [0, 5, 5, 5, 0, 5] as int[], 7,
                [5, 0, 4, 4, 4, 4] as int[], 0
        )
    }


    def "should finish game when one player has no seeds"() {
        given:
        def board = Board.from(
                [0, 0, 0, 0, 0, 1], 24,
                [4, 4, 4, 4, 4, 3], 0
        )

        when:
        def game = Game.create(board)

        then:
        assertBoard(
                game.board,
                [0, 0, 0, 0, 0, 1] as int[], 24,
                [4, 4, 4, 4, 4, 3] as int[], 0
        )

        when:
        def result = game.move(P1, 6)

        then:
        board.players.player1().score() == 25
        board.players.player2().score() == 23
        result.status() == Game.Status.P1_WIN
    }


    def "should allow draw when same seeds"() {
        given:
        def board = Board.from(
                [0, 0, 0, 0, 0, 1], 23,
                [4, 4, 4, 4, 4, 4], 0
        )

        when:
        def game = Game.create(board)

        then:
        assertBoard(
                game.board,
                [0, 0, 0, 0, 0, 1] as int[], 23,
                [4, 4, 4, 4, 4, 4] as int[], 0
        )

        when:
        def result = game.move(P1, 6)

        then:
        board.players.player1().score() == 24
        board.players.player2().score() == 24
        result.status() == Game.Status.DRAW
    }

    def "example play"() {
        given:
        def board = Board.from(
                [3, 2, 2, 1, 1, 3], 6,
                [2, 0, 4, 2, 2, 2], 7
        )

        when:
        def game = Game.create(board)

        then:
        assertBoard(
                game.board,
                [3, 2, 2, 1, 1, 3] as int[], 6,
                [2, 0, 4, 2, 2, 2] as int[], 7
        )

        when:
        game.move(P1, 4)
        game.move(P2, 6)
        game.move(P1, 5)
        def result = game.move(P1, 2)

        then:
        assertBoard(
                result.board(),
                [4, 0, 3, 0, 0, 4] as int[], 12,
                [2, 0, 0, 2, 2, 0] as int[], 8
        )
    }

    private void assertBoard(Board board, int[] p1HouseSeedCnt, int p1StoreSeedCnt, int[] p2HouseSeedCnt, int p2StoreSeedCnt) {
        assert board.houses[0].seedCount == p1HouseSeedCnt[0]
        assert board.houses[1].seedCount == p1HouseSeedCnt[1]
        assert board.houses[2].seedCount == p1HouseSeedCnt[2]
        assert board.houses[3].seedCount == p1HouseSeedCnt[3]
        assert board.houses[4].seedCount == p1HouseSeedCnt[4]
        assert board.houses[5].seedCount == p1HouseSeedCnt[5]
        assert board.stores[0].seedCount == p1StoreSeedCnt

        assert board.houses[6].seedCount == p2HouseSeedCnt[0]
        assert board.houses[7].seedCount == p2HouseSeedCnt[1]
        assert board.houses[8].seedCount == p2HouseSeedCnt[2]
        assert board.houses[9].seedCount == p2HouseSeedCnt[3]
        assert board.houses[10].seedCount == p2HouseSeedCnt[4]
        assert board.houses[11].seedCount == p2HouseSeedCnt[5]
        assert board.stores[1].seedCount == p2StoreSeedCnt
    }
}
