package com.bol.kalaha.core


import spock.lang.Specification

import static com.bol.kalaha.core.PlayerID.P1

class PitSpec extends Specification {
    def "should store seeds in house"() {
        when:
        def house = new House(P1, 4)

        then:
        house.seedCount == 4
    }


    def "store should begin empty"() {
        when:
        def endZone = new Store(P1)

        then:
        endZone.seedCount == 0
    }


    def "should point to the next pit"() {
        given:
        def a = new House(P1, 4)
        def b = new Store(P1)
        def c = new House(P1, 4)

        when:
        a.next = b
        b.next = c

        then:
        a.next == b
        b.next == c
    }


    def "should be able to take seeds from house"() {
        when:
        def house = new House(P1, 4)

        then:
        house.seedCount == 4

        when:
        def taken = house.take()

        then:
        taken == 4
        house.seedCount == 0
    }


    def "should be able to sow seed in house"() {
        given:
        def house = new House(P1, 0)

        when:
        house.sow()

        then:
        house.seedCount == 1
    }


    def "should be able to sow seed in store"() {
        given:
        def store = new Store(P1)

        when:
        store.sow()

        then:
        store.seedCount == 1
    }


    def "should be able to sow multiple seeds in store"() {
        given:
        def store = new Store(P1)

        when:
        store.sow(4)

        then:
        store.seedCount == 4
    }
}
