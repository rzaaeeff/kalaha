package com.bol.kalaha.core;

import java.util.Optional;

public class House extends Pit {

    private House opposite;

    House(PlayerID player, int seeds) {
        super(player, seeds);
    }

    @Override
    public Integer take() {
        int seeds = this.seedCount;
        this.seedCount = 0;
        return seeds;
    }

    @Override
    public Optional<House> getOpposite() {
        return Optional.ofNullable(opposite);
    }

    public void setOpposite(House opposite) {
        this.opposite = opposite;
    }

    boolean isSowableBy(PlayerID player) {
        return true;
    }
}
