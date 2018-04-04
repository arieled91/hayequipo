package org.arieled91.hayequipo.game.exception;

public final class PlayerAlreadyJoinedException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    public PlayerAlreadyJoinedException() {
        super("Player already joined");
    }

    public PlayerAlreadyJoinedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PlayerAlreadyJoinedException(final String message) {
        super(message);
    }

    public PlayerAlreadyJoinedException(final Throwable cause) {
        super(cause);
    }
}

