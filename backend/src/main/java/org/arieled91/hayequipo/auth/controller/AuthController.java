package org.arieled91.hayequipo.auth.controller;

import org.arieled91.hayequipo.EnvProperties;
import org.arieled91.hayequipo.auth.exception.AuthorizationException;
import org.arieled91.hayequipo.auth.exception.UserAlreadyExistsException;
import org.arieled91.hayequipo.auth.model.AuthenticationRequest;
import org.arieled91.hayequipo.auth.model.AuthenticationResponse;
import org.arieled91.hayequipo.auth.model.User;
import org.arieled91.hayequipo.auth.model.UserRequest;
import org.arieled91.hayequipo.auth.service.AuthService;
import org.arieled91.hayequipo.common.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Set;


@Controller
public class AuthController {

    private final AuthService authService;
    private final MessageSource messages;
    private final EnvProperties envProperties;

    @Autowired
    public AuthController(AuthService authService, @Qualifier("messageSource") MessageSource messages, EnvProperties envProperties) {
        this.authService = authService;
        this.messages = messages;
        this.envProperties = envProperties;
    }

    @RequestMapping(value = "/auth/users", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Page<User>> listActiveUsers(@Param("onlyActive") Boolean onlyActive, Pageable pageable) {
        if(onlyActive==null || onlyActive)
            return ResponseEntity.ok(authService.listAll(pageable, true));
        return ResponseEntity.ok(authService.listAll(pageable));
    }

    @RequestMapping(value = "/auth/users/current", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<User> currentUser() {
        return ResponseEntity.ok(authService.getCurrentUser().orElse(new User()));
    }

    @RequestMapping(value = "/auth/users/current/privileges", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Set<String>> currentUserPrivileges() {
        return ResponseEntity.ok(authService.getCurrentUser().orElse(new User()).getPrivileges());
    }
    
    @RequestMapping(value = "/auth/users/current/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUsername() {
        return authService.getCurrentUser().map(User::getEmail).orElse("guest");
    }

    @RequestMapping(value = "/auth/users/current/upgrade", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> upgradeUser(final String email) {
        try {
            authService.upgradeUser(email);
            return ResponseEntity.ok().build();
        }catch (final AuthorizationException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    @RequestMapping(value = "/api/ping", method = RequestMethod.GET)
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok().build();
    }

    @CrossOrigin
    @RequestMapping(value = "/auth/registration", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> registerUserAccount(@RequestBody final UserRequest userRequest, final HttpServletRequest httpRequest) {
        logger.debug("Registering user account with information: {}", userRequest);

        try {
            final User userRegistered = authService.registerOwnUserAccount(userRequest, httpRequest);
            return ResponseEntity.ok(userRegistered);
        }catch (final UserAlreadyExistsException e){
            final Locale locale = httpRequest.getLocale();
            return ResponseEntity.badRequest().body(new GenericResponse(messages.getMessage("message.emailInvalidEmail",null,locale),"userAlreadyExists"));
        }catch (final Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/auth/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(final HttpServletRequest request, final Model model, @RequestParam("token") final String token) throws UnsupportedEncodingException {
        final Locale locale = request.getLocale();
        final String result = authService.validateVerificationToken(token);
        if (AuthService.Token.VALID.key().equals(result)) {
            final User user = authService.getUser(token);
            if(user!=null){
                final String successMessage = messages.getMessage("label.successRegister.title", null, locale);
                return "redirect:"+envProperties.getFrontendUrl()+"/#/login?message="+successMessage+"&token="+authService.buildToken(user.getUsername());
            }
        }

        final String errorMessage = messages.getMessage("auth.message." + result, null, locale);
        return "redirect:"+envProperties.getFrontendUrl()+"/#/login?message="+errorMessage;
    }

    @CrossOrigin
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authentication, final Locale locale) throws AuthenticationException {
        try {
            return ResponseEntity.ok(new AuthenticationResponse(authService.authenticateOwn(authentication)));
        }catch (final AuthenticationException e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(new GenericResponse(messages.getMessage("auth.message.invalidUserOrPass",null, locale)));
        }catch (final Exception e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(new GenericResponse("Unknown error"));
        }
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());
}