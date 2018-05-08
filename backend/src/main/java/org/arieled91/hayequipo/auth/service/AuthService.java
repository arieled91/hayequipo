package org.arieled91.hayequipo.auth.service;

import org.arieled91.hayequipo.auth.exception.AuthorizationException;
import org.arieled91.hayequipo.auth.model.*;
import org.arieled91.hayequipo.auth.repository.RoleRepository;
import org.arieled91.hayequipo.auth.repository.UserRepository;
import org.arieled91.hayequipo.auth.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;


@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository tokenRepository;
    private final OAuth2AuthorizedClientService clientService;

    @Autowired
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, VerificationTokenRepository tokenRepository, OAuth2AuthorizedClientService clientService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.clientService = clientService;
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

    public Optional<User> findUserByToken(String token){
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if(verificationToken!=null) return Optional.ofNullable(verificationToken.getUser());
        return Optional.empty();
    }

    public VerificationToken authenticate(Authentication authentication){
        final User user = findOrCreateUser(authentication);
        final OAuth2AccessToken token = extractToken(authentication);
        final LocalDateTime expiration = LocalDateTime.ofInstant(token.getExpiresAt(), ZoneId.systemDefault());

        return tokenRepository.save(new VerificationToken(token.getTokenValue(), user, expiration));
    }

    public User findOrCreateUser(Authentication authentication){
        final OidcUserInfo userInfo = ((DefaultOidcUser) authentication.getPrincipal()).getUserInfo();

        //if another oauth2 provider is added think what to do when same email is used from different providers
        return findActiveUserByMail(userInfo.getEmail()).orElseGet(()-> registerUserAccount(userInfo));
    }

    public OAuth2AccessToken extractToken(Authentication authentication){
        final OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        final OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());

        return client.getAccessToken();
    }

    public User registerUserAccount(OidcUserInfo userInfo) {
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
}
