package org.arieled91.hayequipo.auth.controller;

import org.arieled91.hayequipo.auth.OnRegistrationConfirmEvent;
import org.arieled91.hayequipo.auth.OnRegistrationEvent;
import org.arieled91.hayequipo.auth.exception.AuthorizationException;
import org.arieled91.hayequipo.auth.exception.InvalidOldPasswordException;
import org.arieled91.hayequipo.auth.exception.UserAlreadyExistsException;
import org.arieled91.hayequipo.auth.exception.UserNotFoundException;
import org.arieled91.hayequipo.auth.model.Privilege;
import org.arieled91.hayequipo.auth.model.Role;
import org.arieled91.hayequipo.auth.model.User;
import org.arieled91.hayequipo.auth.model.VerificationToken;
import org.arieled91.hayequipo.auth.model.dto.AuthenticationRequest;
import org.arieled91.hayequipo.auth.model.dto.AuthenticationResponse;
import org.arieled91.hayequipo.auth.model.dto.GenericResponse;
import org.arieled91.hayequipo.auth.model.dto.Password;
import org.arieled91.hayequipo.auth.model.dto.UserRequest;
import org.arieled91.hayequipo.auth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;


@Controller
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final UserService userService;

    private final MessageSource messages;

    private final JavaMailSender mailSender;

    private final ApplicationEventPublisher eventPublisher;

    private final Environment env;

    private final AuthenticationManager authenticationManager;

    @Value("${backend.url}")
    private final String backendUrl = "";



    @Autowired
    public AuthController(UserService userService, MessageSource messages, JavaMailSender mailSender, ApplicationEventPublisher eventPublisher, Environment env, AuthenticationManager authenticationManager) {
        super();
        this.userService = userService;
        this.messages = messages;
        this.mailSender = mailSender;
        this.eventPublisher = eventPublisher;
        this.env = env;
        this.authenticationManager=authenticationManager;
    }

    // Registration

    @RequestMapping(value = "/auth/formRegistration", method = RequestMethod.POST)
    public String formRegisterUserAccount(final @Valid UserRequest accountDto, final HttpServletRequest request) {
        logger.debug("Registering user account with information: {}", accountDto);

        try {
            final User registered = userService.registerNewUserAccount(accountDto);
            eventPublisher.publishEvent(new OnRegistrationEvent(registered, request.getLocale(), getAppUrl(request)));
            return "redirect:/registration?sent";
        }catch (final UserAlreadyExistsException e){
            return "redirect:/registration?emailError";
        }catch (final Exception e){
            return "redirect:/registration?error";
        }
    }

    @RequestMapping(value = "/auth/registration", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> registerUserAccount(@RequestBody final UserRequest userRequest, final HttpServletRequest request) {
        logger.debug("Registering user account with information: {}", userRequest);

        try {
            final User registered = userService.registerNewUserAccount(userRequest);
            eventPublisher.publishEvent(new OnRegistrationEvent(registered, request.getLocale(), getAppUrl(request)));
            return ResponseEntity.ok().build();
        }catch (final UserAlreadyExistsException e){
            final Locale locale = request.getLocale();
            return ResponseEntity.badRequest().body(new GenericResponse(messages.getMessage("message.emailInvalidEmail",null,locale),"userAlreadyExists"));
        }catch (final Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequestMapping(value = "/auth/user", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<User> currentUser() {
        return ResponseEntity.ok(userService.getCurrentUser().orElse(new User()));
    }
    
    @RequestMapping(value = "/auth/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUsername() {
        return userService.getCurrentUser().map(User::getEmail).orElse("guest");
    }

    @RequestMapping(value = "/auth/upgrade", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> upgradeUser(final String email) {
        try {
            userService.upgradeUser(email);
            return ResponseEntity.ok().build();
        }catch (final AuthorizationException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @RequestMapping(value = "/auth/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(final HttpServletRequest request, final Model model, @RequestParam("token") final String token, RedirectAttributes redirect) throws UnsupportedEncodingException {
        final Locale locale = request.getLocale();
        final String result = userService.validateVerificationToken(token);
        if (UserService.Token.VALID.key().equals(result)) {
            final User user = userService.getUser(token);
            // if (user.isUsing2FA()) {
            // model.addAttribute("qr", userService.generateQRUrl(user));
            // return "redirect:/qrcode.html?lang=" + locale.getLanguage();
            // }
            if(user!=null) {
                authWithoutPassword(user);
                eventPublisher.publishEvent(new OnRegistrationConfirmEvent(user));
            }
//            model.addAttribute("message", messages.getMessage("message.accountVerified", null, locale));
//            return "redirect:/login?signedup";
            return "redirect:/info?registrationComplete";
        }

        redirect.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
        redirect.addAttribute("expired", UserService.Token.EXPIRED.key().equals(result));
        redirect.addAttribute("token", token);
        return "redirect:/error";
    }

    // user activation - verification

    @RequestMapping(value = "/auth/resendRegistrationToken", method = RequestMethod.GET)
    @ResponseBody
    public GenericResponse resendRegistrationToken(final HttpServletRequest request, @RequestParam("token") final String existingToken) {
        final VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
        final User user = userService.getUser(newToken.getToken());
        mailSender.send(constructResendVerificationTokenEmail(getAppUrl(request), request.getLocale(), newToken, user));
        return new GenericResponse(messages.getMessage("message.resendToken", null, request.getLocale()));
    }

    @RequestMapping(value = "/api/ping", method = RequestMethod.GET)
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok().build();
    }

//
//    @RequestMapping(value = "/auth/resetPassword", method = RequestMethod.POST)
//    @ResponseBody
//    public GenericResponse resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail) {
//        final User user = userService.findUserByEmail(userEmail);
//        if (user != null) {
//            final String token = UUID.randomUUID()
//                    .toString();
//            userService.createPasswordResetTokenForUser(user, token);
//            mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
//        }
//        return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
//    }

//    @RequestMapping(value = "/auth/changePassword", method = RequestMethod.GET)
//    public String showChangePasswordPage(final Locale locale, final Model model, @RequestParam("id") final long id, @RequestParam("token") final String token) {
//        final String result = securityUserService.validatePasswordResetToken(id, token);
//        if (result != null) {
//            model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
//            return "redirect:/login?lang=" + locale.getLanguage();
//        }
//        return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
//    }

    @RequestMapping(value = "/auth/savePassword", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse savePassword(final Locale locale, @Valid Password password) {
        final User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        userService.changeUserPassword(user, password.getNewPassword());
        return new GenericResponse(messages.getMessage("message.resetPasswordSuc", null, locale));
    }

    // change user password
    @RequestMapping(value = "/auth/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse changeUserPassword(final Locale locale, @Valid Password password) {
        final String email = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
        final User user = userService.findActiveUserByMail(email).orElseThrow(UserNotFoundException::new);

        if (!userService.checkIfValidOldPassword(user, password.getOldPassword())) {
            throw new InvalidOldPasswordException();
        }
        userService.changeUserPassword(user, password.getNewPassword());
        return new GenericResponse(messages.getMessage("message.updatePasswordSuc", null, locale));
    }

//    @RequestMapping(value = "/auth/update/2fa", method = RequestMethod.POST)
//    @ResponseBody
//    public GenericResponse modifyUser2FA(@RequestParam("use2FA") final boolean use2FA) throws UnsupportedEncodingException {
//        final User user = userService.updateUser2FA(use2FA);
//        if (use2FA) {
//            return new GenericResponse(userService.generateQRUrl(user));
//        }
//        return null;
//    }


    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authentication, Device device, final Locale locale) throws AuthenticationException {
        try {
            return ResponseEntity.ok(new AuthenticationResponse(userService.autenticate(authentication.getUsername(), authentication.getPassword(), device)));
        }catch (final AuthenticationException e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(new GenericResponse(messages.getMessage("auth.message.invalidUserOrPass",null, locale)));
        }catch (final Exception e){
            logger.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(new GenericResponse("Unknown error"));
        }
    }

    @RequestMapping(value = "/auth/formLogin", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> formLogin(AuthenticationRequest authenticationRequest, Device device, final Locale locale) throws AuthenticationException {
        return login(authenticationRequest, device, locale);
    }



    // ============== NON-API ============

    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final User user) {
        final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
    }

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final User user) {
        final String url = contextPath + "/auth/changePassword?id=" + user.getId() + "&token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(requireNonNull(env.getProperty("spring.mail.username"), MAIL_FROM_PROPERTY_ERROR));
        return email;
    }

    private String getAppUrl(HttpServletRequest request) {
        final String url = backendUrl != null && !backendUrl.isEmpty() ? backendUrl : "http://" + request.getServerName() + ":" + request.getServerPort();
        return url + request.getContextPath();
    }


    public void authWithAuthManager(HttpServletRequest request, String username, String password) {
        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        authToken.setDetails(new WebAuthenticationDetails(request));
        final Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        // request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }

    public void authWithHttpServletRequest(HttpServletRequest request, String username, String password) {
        try {
            request.login(username, password);
        } catch (final ServletException e) {
            logger.error("Error while login ", e);
        }
    }

    private void authWithoutPassword(User user){
        final List<Privilege> privileges = user.getRoles()
                .stream()
                .map(Role::getPrivileges)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        final List<GrantedAuthority> authorities = privileges.stream()
                .map(p -> new SimpleGrantedAuthority(p.getName()))
                .collect(Collectors.toList());

        final Authentication authentication =
                new UsernamePasswordAuthenticationToken(user.getUsername(), null,authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private static final String MAIL_FROM_PROPERTY_ERROR = "Email from property cannot be null";
}