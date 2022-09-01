package com.bol.kalaha.core.model

import com.bol.kalaha.core.exception.IllegalMoveException
import spock.lang.Specification

import static com.bol.kalaha.core.model.PlayerID.P1
import static com.bol.kalaha.core.model.PlayerID.P2

class PlayerSpec extends Specification {
    def "player should sow seeds on turn"() {
        given:
        def last = new House(P1, 0)
        def middle = new House(P1, 0)
        def first = new House(P1, 2)
        def end = new Store(P1)

        first.setNext(middle).setNext(last).setNext(end)
        def player = new Player(P1, [first, middle, last], end)

        when:
        def landed = player.turn(1)

        then:
        landed == last
        first.seedCount == 0
        middle.seedCount == 1
        last.seedCount == 1
    }

    def "player cannot choose empty house"() {
        given:
        def player = new Player(P1, [new House(P1, 0)], new Store(P1))

        when:
        player.turn(1)

        then:
        thrown(IllegalMoveException.class)
    }

    def "player cannot choose house below range"() {
        given:
        def player = new Player(P1, [new House(P1, 0)], new Store(P1))

        when:
        player.turn(0)

        then:
        thrown(IllegalMoveException.class)
    }


    def "player skips opponents store"() {
        given:
        def myHouse = new House(P1, 3)
        def myStore = new Store(P1)
        def opponentHouse = new House(P2, 0)
        def opponentStore = new Store(P2)

        myHouse.setNext(myStore).setNext(opponentHouse).setNext(opponentStore).setNext(myHouse)

        def player = new Player(P1, [myHouse], myStore)

        when:
        def landed = player.turn(1)

        then:
        landed == myHouse
        myHouse.seedCount == 1
        myStore.seedCount == 1
        opponentHouse.seedCount == 1
        opponentStore.seedCount == 0
    }

    def "player cannot capture own house"() {
        given:
        def myHouse = new House(P1, 2)
        def myStore = new Store(P1)
        def opponentHouse = new House(P2, 0)
        def opponentStore = new Store(P2)

        opponentHouse.setOpposite(myHouse)

        myHouse.setNext(myStore).setNext(opponentHouse).setNext(opponentStore).setNext(myHouse)

        def player = new Player(P1, [myHouse], myStore)

        when:
        def landed = player.turn(1)

        then:
        landed == opponentHouse
        myHouse.seedCount == 0
        myStore.seedCount == 1
        opponentHouse.seedCount == 1
        opponentStore.seedCount == 0
    }

    def "player cannot capture empty house"() {
        given:
        def myHouses = [new House(P1, 1), new House(P1, 0)]
        def myStore = new Store(P1)
        def opponentHouse = new House(P2, 0)
        def opponentStore = new Store(P2)

        opponentHouse.setOpposite(myHouses[1])

        myHouses[0].setNext(myHouses[1])

        def player = new Player(P1, myHouses, myStore)

        when:
        def landed = player.turn(1)

        then:
        landed == myHouses[1]
        myHouses[0].seedCount == 0
        myHouses[1].seedCount == 1
        myStore.seedCount == 0
        opponentHouse.seedCount == 0
        opponentStore.seedCount == 0
    }
}
