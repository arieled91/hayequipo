package org.arieled91.hayequipo.auth;

import org.arieled91.hayequipo.auth.model.User;
import org.springframework.context.ApplicationEvent;

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
