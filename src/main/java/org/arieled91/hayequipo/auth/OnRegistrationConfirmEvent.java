package org.arieled91.hayequipo.auth;

import org.arieled91.hayequipo.auth.model.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@SuppressWarnings("serial")
public class OnRegistrationConfirmEvent extends ApplicationEvent {

    private final User user;

    public OnRegistrationConfirmEvent(final User user) {
        super(user);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
