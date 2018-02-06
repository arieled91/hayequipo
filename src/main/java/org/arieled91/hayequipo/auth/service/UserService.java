package org.arieled91.hayequipo.auth.service;

import org.arieled91.hayequipo.auth.exception.AuthorizationException;
import org.arieled91.hayequipo.auth.exception.UserAlreadyExistsException;
import org.arieled91.hayequipo.auth.model.*;
import org.arieled91.hayequipo.auth.repository.RoleRepository;
import org.arieled91.hayequipo.auth.repository.UserRepository;
import org.arieled91.hayequipo.auth.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final SessionRegistry sessionRegistry;

    private final VerificationTokenRepository tokenRepository;

//    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
//    public static String APP_NAME = "SpringRegistration";

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, SessionRegistry sessionRegistry, VerificationTokenRepository tokenRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.sessionRegistry = sessionRegistry;
        this.tokenRepository = tokenRepository;
    }

    // API
    
    public User registerNewUserAccount(final User user) {
        if(findActiveUserByMail(user.getEmail()).isPresent()) throw new UserAlreadyExistsException();

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(roleRepository.findByName("ROLE_USER")));
        return repository.save(user);
    }

    public User getUser(final String verificationToken) {
        final VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }

    public VerificationToken getVerificationToken(final String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    public void saveRegisteredUser(final User user) {
        repository.save(user);
    }

    public void deleteUser(final User user) {
        final VerificationToken verificationToken = tokenRepository.findByUser(user);

        if (verificationToken != null) {
            tokenRepository.delete(verificationToken);
        }

//        final PasswordResetToken passwordToken = passwordTokenRepository.findByUser(user);
//
//        if (passwordToken != null) {
//            passwordTokenRepository.delete(passwordToken);
//        }

        repository.delete(user);
    }

    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID().toString());
        vToken = tokenRepository.save(vToken);
        return vToken;
    }

//    @Override
//    public void createPasswordResetTokenForUser(final User user, final String token) {
//        final PasswordResetToken myToken = new PasswordResetToken(token, user);
//        passwordTokenRepository.save(myToken);
//    }

//    @Override
//    public PasswordResetToken getPasswordResetToken(final String token) {
//        return passwordTokenRepository.findByToken(token);
//    }

//    @Override
//    public User getUserByPasswordResetToken(final String token) {
//        return passwordTokenRepository.findByToken(token).getUser();
//    }

    public Optional<User> getUserById(final long id) {
        return repository.findById(id);
    }

    public void changeUserPassword(final User user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        repository.save(user);
    }

    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return Token.INVALID.key();
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            return Token.EXPIRED.key;
        }

        user.setEnabled(true);
        // tokenRepository.delete(verificationToken);
        repository.save(user);
        return Token.VALID.key();
    }

//    @Override
//    public String generateQRUrl(User user) throws UnsupportedEncodingException {
//        return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME, user.getEmail(), user.getSecret(), APP_NAME), "UTF-8");
//    }

//    @Override
//    public User updateUser2FA(boolean use2FA) {
//        final Authentication curAuth = SecurityContextHolder.getContext().getAuthentication();
//        User currentUser = (User) curAuth.getPrincipal();
//        currentUser.setUsing2FA(use2FA);
//        currentUser = repository.save(currentUser);
//        final Authentication auth = new UsernamePasswordAuthenticationToken(currentUser, currentUser.getPassword(), curAuth.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(auth);
//        return currentUser;
//    }

    public Optional<User> findActiveUserByMail(final String email) {
        return Optional.ofNullable(repository.findByEmailAndEnabledIsTrue(email));
    }

    public List<String> getUsersFromSessionRegistry() {
        return sessionRegistry.getAllPrincipals().stream().filter((u) -> !sessionRegistry.getAllSessions(u, false).isEmpty()).map(Object::toString).collect(Collectors.toList());
    }

    public Optional<User> getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken))
            return findActiveUserByMail(authentication.getName());
        return Optional.empty();
    }

    public Set<String> getCurrentUserPrivileges(){
        return getCurrentUser().map(this::getPrivileges).orElse(Collections.emptySet());
    }

    public Set<String> getPrivileges(final User user){
       return user.getRoles().stream()
               .flatMap(r -> r.getPrivileges().stream().map(Privilege::getName))
               .collect(Collectors.toSet());
    }

    public boolean currentUserHasPrivilege(PrivilegeType privilegeType){
        return getCurrentUser().map(user -> getPrivileges(user).contains(privilegeType.name())).orElse(false);
    }
    public boolean hasPrivilege(User user, PrivilegeType privilegeType){
        return getPrivileges(user).contains(privilegeType.name());
    }

    public void upgradeUser(String email){
        findActiveUserByMail(email).ifPresent(this::upgradeUser);
    }

    public void upgradeUser(User user){
        Role moderator = roleRepository.findByName(RoleType.ROLE_MODERATOR.name());
        if(moderator==null) throw new RuntimeException("Moderator role not found");

        if(currentUserHasPrivilege(PrivilegeType.FULL_ACCESS_PRIVILEGE)) {
            user.getRoles().add(moderator);
            repository.save(user);
        }
        else throw new AuthorizationException();
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
