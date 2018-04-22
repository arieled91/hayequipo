package org.arieled91.hayequipo.auth.service;

import org.arieled91.hayequipo.auth.model.*;
import org.arieled91.hayequipo.auth.model.dto.AuthToken;
import org.arieled91.hayequipo.auth.repository.RoleRepository;
import org.arieled91.hayequipo.auth.repository.UserRepository;
import org.arieled91.hayequipo.auth.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;


@Service
@Transactional
public class AuthService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository tokenRepository;

    @Autowired
    public AuthService(UserRepository repository, RoleRepository roleRepository, VerificationTokenRepository tokenRepository) {
        this.userRepository = repository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
    }

//    public User registerNewUserAccount(final UserRequest request) {
//        if(findActiveUserByMail(request.getEmail()).isPresent()) throw new UserAlreadyExistsException();
//
//        final User user = new User();
//        user.setEmail(requireNonNull(request.getEmail(),"Email cannot be null"));
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
////        user.setPassword(passwordEncoder.encode(Objects.requireNonNull(request.getPassword(),"Password cannot be null")));
//        user.setPassword(request.getPassword()); //todo delete?
//        user.setRoles(new HashSet<>(Collections.singletonList(roleRepository.findByName("ROLE_USER"))));
//        return userRepository.save(user);
//    }

    public Cookie authenticate(Authentication authentication){
        final OidcUserInfo userInfo = ((DefaultOidcUser) authentication.getPrincipal()).getUserInfo();

        //if another oauth2 provider is added think what to do when same email is used from different providers
        final User user = findActiveUserByMail(userInfo.getEmail()).orElseGet(()-> registerUserAccount(userInfo));

        final OidcIdToken token = ((DefaultOidcUser) authentication.getPrincipal()).getIdToken();
        final LocalDateTime expiration = LocalDateTime.ofInstant(token.getExpiresAt(), ZoneId.systemDefault());
        tokenRepository.save(new VerificationToken(token.getTokenValue(), user, expiration));

        return new Cookie("currentUser",new AuthToken(user.getUsername(), token.getTokenValue()).toBase64());
    }

    public User registerUserAccount(OidcUserInfo userInfo) {
        final User user = new User();
        user.setEmail(requireNonNull(userInfo.getEmail(),"Email cannot be null"));
        user.setFirstName(userInfo.getGivenName());
        user.setLastName(userInfo.getFamilyName());
        user.setPassword("empty");
        user.setEnabled(true);
        user.setTokenExpired(false);
        user.setRoles(new HashSet<>(Collections.singletonList(roleRepository.findByName(RoleType.ROLE_USER.name()))));
        return userRepository.save(user);
    }

    public Optional<User> getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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
