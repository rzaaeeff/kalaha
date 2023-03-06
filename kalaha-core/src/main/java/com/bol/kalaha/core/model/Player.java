package com.bol.kalaha.core.model;

import com.bol.kalaha.core.exception.IllegalMoveException;

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
        return pit.getSeedCount() == 1 &&
                pit.getOpposite().isPresent() &&
                pit.getOpposite().get().getPlayer() != this.id() &&
                pit.getOpposite().get().getSeedCount() > 0;
    }

    private void checkHasSeeds(House house) {
        if (house.isEmpty()) {
            throw new IllegalMoveException("Chosen house doesn't have any seed");
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
            throw new IllegalMoveException("Player must choose their own house");
        }
        return this.houses.get(houseNum - 1);
    }
}
