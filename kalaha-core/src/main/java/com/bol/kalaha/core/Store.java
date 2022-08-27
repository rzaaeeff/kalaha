package com.bol.kalaha.core;

public class Store extends Pit {
    Store(PlayerID player) {
        super(player, 0);
    }

    Store(PlayerID player, int seeds) {
        super(player, seeds);
    }

    public void sow(int i) {
        seedCount += i;
    }

    boolean isSowableBy(PlayerID player) {
        return player.equals(this.player);
    }
}
