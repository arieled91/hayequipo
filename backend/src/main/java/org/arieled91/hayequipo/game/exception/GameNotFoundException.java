package org.arieled91.hayequipo.game.exception;

public final class GameNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    public GameNotFoundException() {
        super("Game not found");
    }

    public GameNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public GameNotFoundException(final String message) {
        super(message);
    }

    public GameNotFoundException(final Throwable cause) {
        super(cause);
    }
}

