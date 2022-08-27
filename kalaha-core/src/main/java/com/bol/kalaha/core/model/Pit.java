package com.bol.kalaha.core.model;

import java.util.Optional;

public abstract class Pit {

    protected final PlayerID player;
    protected int seedCount;
    private Pit next;

    Pit(PlayerID player, int seedCount) {
        this.player = player;
        this.seedCount = seedCount;
    }

    public int getSeedCount() {
        return seedCount;
    }

    public Pit getNext() {
        return next;
    }

    public PlayerID getPlayer() {
        return player;
    }

    public void sow() {
        this.seedCount++;
    }

    abstract boolean isSowableBy(PlayerID player);

    public boolean isEmpty() {
        return this.seedCount == 0;
    }

    public Optional<House> getOpposite() {
        return Optional.empty();
    }

    public Integer capture() {
        if (this.getOpposite().isEmpty()) {
            return 0;
        }

        return this.getOpposite().get().take();
    }

    public Integer take() {
        return 0;
    }

    public Pit setNext(Pit next) {
        this.next = next;
        return next;
    }
}
