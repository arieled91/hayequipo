package org.arieled91.hayequipo.game.exception;

public final class GameClosedException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    public GameClosedException() {
        super("Game is closed");
    }

    public GameClosedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public GameClosedException(final String message) {
        super(message);
    }

    public GameClosedException(final Throwable cause) {
        super(cause);
    }
}

