package com.bol.kalaha.core


import spock.lang.Specification

import static com.bol.kalaha.core.PlayerID.P1
import static com.bol.kalaha.core.PlayerID.P2

class BoardSpec extends Specification {
    def board = Board.create()

    def "board should have twelve houses"() {
        given:
        def houses = board.houses

        when:
        def sides = houses.groupBy { it.player }

        then:
        sides[P1].size() == 6
        sides[P2].size() == 6
    }


    def "board should have two stores"() {
        given:
        def stores = board.stores

        when:
        def sides = stores.groupBy { it.player }

        then:
        sides[P1].size() == 1
        sides[P2].size() == 1
    }


    def "board should have two players"() {
        expect:
        board.players.player1().id() == P1
        board.players.player2().id() == P2
    }


    def "houses should have mutual opposites"() {
        given:
        def players = board.players
        def housesOne = players.player1().houses()

        when:
        def housesTwo = players.player2().houses()

        then:
        housesOne[0].opposite.get() == housesTwo[5]
        housesOne[1].opposite.get() == housesTwo[4]
        housesOne[2].opposite.get() == housesTwo[3]
        housesOne[3].opposite.get() == housesTwo[2]
        housesOne[4].opposite.get() == housesTwo[1]
        housesOne[5].opposite.get() == housesTwo[0]
        housesTwo[0].opposite.get() == housesOne[5]
        housesTwo[1].opposite.get() == housesOne[4]
        housesTwo[2].opposite.get() == housesOne[3]
        housesTwo[3].opposite.get() == housesOne[2]
        housesTwo[4].opposite.get() == housesOne[1]
        housesTwo[5].opposite.get() == housesOne[0]
    }


    def "all pits should form a cycle"() {
        given:
        def first = board.houses[0]
        def pit = first

        def all = new HashSet<>()
        all.add(pit)

        when:
        for (int i = 0; i < 14; i++) {
            pit = pit.next
            all.add(pit)
        }

        then:
        pit == first
        all.size() == 14
    }
}
