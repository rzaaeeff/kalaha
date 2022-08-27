package com.bol.kalaha.core


import spock.lang.Specification

import static com.bol.kalaha.core.PlayerID.P1
import static com.bol.kalaha.core.PlayerID.P2

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
        thrown(IllegalArgumentException.class)
    }

    def "player cannot choose house below range"() {
        given:
        def player = new Player(P1, [new House(P1, 0)], new Store(P1))

        when:
        player.turn(0)

        then:
        thrown(IllegalArgumentException.class)
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
}
