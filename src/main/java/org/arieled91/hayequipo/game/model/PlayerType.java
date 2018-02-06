package org.arieled91.hayequipo.game.model;

public enum PlayerType {
    MODERATOR(1),
    NORMAL(2),
    GUEST(3);

    private final int order;

    PlayerType(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
