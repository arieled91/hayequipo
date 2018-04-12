package org.arieled91.hayequipo.auth.service;

import org.arieled91.hayequipo.auth.exception.UserAlreadyExistsException;
import org.arieled91.hayequipo.auth.model.Privilege;
import org.arieled91.hayequipo.auth.model.PrivilegeType;
import org.arieled91.hayequipo.auth.model.User;
import org.arieled91.hayequipo.auth.model.dto.UserRequest;
import org.arieled91.hayequipo.auth.repository.RoleRepository;
import org.arieled91.hayequipo.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class AuthService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public AuthService(UserRepository repository, RoleRepository roleRepository) {
        this.userRepository = repository;
        this.roleRepository = roleRepository;
    }

    public User registerNewUserAccount(final UserRequest request) {
        if(findActiveUserByMail(request.getEmail()).isPresent()) throw new UserAlreadyExistsException();

        final User user = new User();
        user.setEmail(Objects.requireNonNull(request.getEmail(),"Email cannot be null"));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
//        user.setPassword(passwordEncoder.encode(Objects.requireNonNull(request.getPassword(),"Password cannot be null")));
        user.setPassword(request.getPassword()); //todo delete?
        user.setRoles(new HashSet<>(Collections.singletonList(roleRepository.findByName("ROLE_USER"))));
        return userRepository.save(user);
    }

    public User registerUserAccount(Authentication authentication) {
        final OidcUserInfo userInfo = ((DefaultOidcUser) authentication.getPrincipal()).getUserInfo();

        //if another oauth2 provider is added think what to do when same email is used from different providers
        final Optional<User> activeUser = findActiveUserByMail(userInfo.getEmail());
        if(activeUser.isPresent()) return activeUser.get();

        final User user = new User();
        user.setEmail((Objects.requireNonNull(userInfo.getEmail(),"Email cannot be null")));
        user.setFirstName(userInfo.getGivenName());
        user.setLastName(userInfo.getFamilyName());
        user.setPassword("empty");
        user.setEnabled(true);
        user.setTokenExpired(false);
        user.setRoles(new HashSet<>(Collections.singletonList(roleRepository.findByName("ROLE_USER"))));

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
