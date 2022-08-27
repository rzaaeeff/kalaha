package com.bol.kalaha.core;

import java.util.List;

public record Player(PlayerID id, List<House> houses, Store store) {

    public Pit turn(int houseNum) {
        House house = getHouse(houseNum);
        checkHasSeeds(house);
        Pit pit = takeTurn(house);
        if (shouldCaptureOpposite(pit)) {
            store.sow(pit.take());
            store.sow(pit.capture());
        }
        return pit;
    }

    public boolean complete() {
        return houses.stream().allMatch(House::isEmpty);
    }

    public void finish() {
        for (House house : houses) {
            store.sow(house.take());
        }
    }

    public int score() {
        return store.getSeedCount();
    }

    private boolean shouldCaptureOpposite(Pit pit) {
        return pit.getSeedCount() == 1 && pit.getOpposite().isPresent();
    }

    private void checkHasSeeds(House house) {
        if (house.isEmpty()) {
            throw new IllegalArgumentException("House must have seeds to take turn");
        }
    }

    private Pit takeTurn(House house) {
        Integer seeds = house.take();
        Pit pit = house;
        while (seeds > 0) {
            pit = pit.getNext();
            if (pit.isSowableBy(id)) {
                seeds--;
                pit.sow();
            }
        }
        return pit;
    }

    private House getHouse(int houseNum) {
        if (houseNum < 1 || houseNum > houses.size()) {
            throw new IllegalArgumentException("House number must be between 1 and " + houses.size());
        }
        return this.houses.get(houseNum - 1);
    }
}
