package org.arieled91.hayequipo.auth;


import java.util.Objects;
import java.util.UUID;

import org.arieled91.hayequipo.auth.model.User;
import org.arieled91.hayequipo.auth.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private final UserService service;

    private final MessageSource messages;

    private final JavaMailSender mailSender;

    private final Environment env;

    @Autowired
    public RegistrationListener(UserService service, MessageSource messages, JavaMailSender mailSender, Environment env) {
        this.service = service;
        this.messages = messages;
        this.mailSender = mailSender;
        this.env = env;
    }

    // API

    @Override
    public void onApplicationEvent(@NotNull final OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event) {
        final User user = event.getUser();
        final String token = UUID.randomUUID().toString();
        service.createVerificationTokenForUser(user, token);

        final SimpleMailMessage email = constructEmailMessage(event, user, token);
        mailSender.send(email);
    }

    //

    private SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final User user, final String token) {
        final String recipientAddress = user.getEmail();
        final String subject = messages.getMessage("message.mail.subject", null, event.getLocale());
        final String confirmationUrl = event.getAppUrl() + "/registrationConfirm.html?token=" + token;
        final String message =  messages.getMessage("message.mail.text", null, event.getLocale());
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username"), "Email auth property cannot be null"));
        return email;
    }

}