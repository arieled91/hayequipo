package org.arieled91.hayequipo.auth.exception;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException() {
        super("User lacks privileges for this action");
    }

    public AuthorizationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AuthorizationException(final String message) {
        super(message);
    }

    public AuthorizationException(final Throwable cause) {
        super(cause);
    }
}
