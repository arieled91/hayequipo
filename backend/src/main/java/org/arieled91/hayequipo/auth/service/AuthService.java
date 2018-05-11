package org.arieled91.hayequipo.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.arieled91.hayequipo.EnvProperties;
import org.arieled91.hayequipo.auth.OnRegistrationEvent;
import org.arieled91.hayequipo.auth.exception.AuthorizationException;
import org.arieled91.hayequipo.auth.exception.UserAlreadyExistsException;
import org.arieled91.hayequipo.auth.model.*;
import org.arieled91.hayequipo.auth.repository.RoleRepository;
import org.arieled91.hayequipo.auth.repository.UserRepository;
import org.arieled91.hayequipo.auth.repository.VerificationTokenRepository;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.arieled91.hayequipo.auth.SecurityConstants.EXPIRATION_TIME;
import static org.arieled91.hayequipo.auth.SecurityConstants.TOKEN_PREFIX;


@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final EnvProperties envProperties;

    @Autowired
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, VerificationTokenRepository tokenRepository, OAuth2AuthorizedClientService clientService, @Lazy AuthenticationManager authenticationManager, @Lazy PasswordEncoder passwordEncoder, ApplicationEventPublisher eventPublisher, EnvProperties envProperties) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
        this.envProperties = envProperties;
    }

    public Page<User> listAll(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    public Page<User> listAll(Pageable pageable, boolean enabled){
        return userRepository.findAllByEnabled(enabled, pageable);
    }

    public void upgradeUser(String email){
        findActiveUserByMail(email).ifPresent(this::upgradeUser);
    }

    public void upgradeUser(User user){
        final Role moderator = roleRepository.findByName(RoleType.ROLE_MODERATOR.name());
        if(moderator==null) throw new RuntimeException("Moderator role not found");

        if(currentUserHasPrivilege(PrivilegeType.FULL_ACCESS)) {
            user.getRoles().add(moderator);
            userRepository.save(user);
        }
        else throw new AuthorizationException();
    }

    public boolean currentUserHasPrivilege(PrivilegeType privilegeType){
        return getCurrentUser().map(user -> getPrivileges(user).contains(privilegeType.name())).orElse(false);
    }


    public String authenticateOauth2(Authentication authentication){
        final User user = findOrCreateUser(authentication);
        return buildToken(user.getUsername());
    }

    public String authenticateOwn(AuthenticationRequest authRequest) throws IOException, ServletException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword(),
                        new ArrayList<>()));

        return buildToken(((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername());
    }

    public String buildToken(String username){
        return TOKEN_PREFIX + Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, envProperties.getAuthSecret().getBytes())
                .compact();
    }

    public User findOrCreateUser(Authentication authentication){
        final OidcUserInfo userInfo = ((DefaultOidcUser) authentication.getPrincipal()).getUserInfo();

        //if another oauth2 provider is added think what to do when same email is used from different providers
        return findActiveUserByMail(userInfo.getEmail()).orElseGet(()-> registerOauth2UserAccount(userInfo));
    }


    public User registerOauth2UserAccount(OidcUserInfo userInfo) {
        final User user = new User();
        user.setEmail(requireNonNull(userInfo.getEmail(),"Email cannot be null"));
        user.setFirstName(userInfo.getGivenName());
        user.setLastName(userInfo.getFamilyName());
        user.setPassword(UUID.randomUUID().toString());
        user.setEnabled(true);
        user.setTokenExpired(false);
        user.setRoles(new HashSet<>(Collections.singletonList(roleRepository.findByName(RoleType.ROLE_USER.name()))));
        return userRepository.save(user);
    }

    public Optional<User> getCurrentUser(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken))
            return findActiveUserByMail(authentication.getName());
        return Optional.empty();
    }

    public Optional<User> findActiveUserByMail(final String email) {
        return Optional.ofNullable(userRepository.findByEmailAndEnabledIsTrue(email));
    }

    public boolean hasPrivilege(User user, PrivilegeType privilegeType){
        return getPrivileges(user).contains(privilegeType.name());
    }

    public Set<String> getPrivileges(final User user){
        return user.getRoles().stream()
                .flatMap(r -> r.getPrivileges().stream().map(Privilege::getName))
                .collect(Collectors.toSet());
    }

    public User registerOwnUserAccount(final UserRequest userRequest, HttpServletRequest httpRequest) {
        if(findActiveUserByMail(userRequest.getEmail()).isPresent()) throw new UserAlreadyExistsException();

        final User user = new User();
        user.setEmail(Objects.requireNonNull(userRequest.getEmail(),"Email cannot be null"));
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPassword(passwordEncoder.encode(Objects.requireNonNull(userRequest.getPassword(),"Password cannot be null")));
        user.setRoles(new HashSet<>(Collections.singletonList(roleRepository.findByName("ROLE_USER"))));
        final User registered = userRepository.save(user);

        eventPublisher.publishEvent(new OnRegistrationEvent(registered, httpRequest.getLocale(), getAppUrl(httpRequest)));

        return registered;
    }

    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return Token.INVALID.key();
        }

        final User user = verificationToken.getUser();
        if ((verificationToken.getExpiryDate().isBefore(LocalDateTime.now()))) {
            tokenRepository.delete(verificationToken);
            return Token.EXPIRED.key;
        }

        user.setEnabled(true);
        userRepository.save(user);
//        tokenRepository.delete(verificationToken);
        return Token.VALID.key();
    }

    public @Nullable User getUser(final String verificationToken) {
        final VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }

    public enum Token{
        INVALID("invalidToken"),
        EXPIRED("expired"),
        VALID("valid");

        private String key;

        Token(String key) {
            this.key=key;
        }

        public String key() {
            return key;
        }
    }
}
