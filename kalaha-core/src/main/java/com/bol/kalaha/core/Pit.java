package com.bol.kalaha.core;

import lombok.Getter;

import java.util.Optional;

@Getter
public abstract class Pit {

    protected int seedCount;

    private Pit next;

    protected final PlayerID player;

    Pit(PlayerID player, int seedCount) {
        this.player = player;
        this.seedCount = seedCount;
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
