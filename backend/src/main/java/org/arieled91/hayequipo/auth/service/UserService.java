package org.arieled91.hayequipo.auth.service;

import org.arieled91.hayequipo.auth.TokenUtil;
import org.arieled91.hayequipo.auth.exception.AuthorizationException;
import org.arieled91.hayequipo.auth.exception.UserAlreadyExistsException;
import org.arieled91.hayequipo.auth.model.*;
import org.arieled91.hayequipo.auth.model.dto.UserRequest;
import org.arieled91.hayequipo.auth.repository.RoleRepository;
import org.arieled91.hayequipo.auth.repository.UserRepository;
import org.arieled91.hayequipo.auth.repository.VerificationTokenRepository;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

//@Service
//@Transactional
public class UserService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

//    private final SessionRegistry sessionRegistry;

    private final VerificationTokenRepository tokenRepository;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final TokenUtil tokenUtil;

//    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
//    public static String APP_NAME = "SpringRegistration";

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, RoleRepository roleRepository/*, SessionRegistry sessionRegistry*/, VerificationTokenRepository tokenRepository, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, TokenUtil tokenUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
//        this.sessionRegistry = sessionRegistry;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenUtil = tokenUtil;
    }

    // API

    public String autenticate(final String username, final String password, Device device) throws AuthenticationException {
        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return tokenUtil.generateTokenWithHeader(userDetails, device);
    }
    
    public User registerNewUserAccount(final UserRequest request) {
        if(findActiveUserByMail(request.getEmail()).isPresent()) throw new UserAlreadyExistsException();

        final User user = new User();
        user.setEmail(Objects.requireNonNull(request.getEmail(),"Email cannot be null"));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(Objects.requireNonNull(request.getPassword(),"Password cannot be null")));
        user.setRoles(new HashSet<>(Collections.singletonList(roleRepository.findByName("ROLE_USER"))));
        return repository.save(user);
    }

    public @Nullable User getUser(final String verificationToken) {
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
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
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

//    public List<String> getUsersFromSessionRegistry() {
//        return sessionRegistry.getAllPrincipals().stream().filter((u) -> !sessionRegistry.getAllSessions(u, false).isEmpty()).map(Object::toString).collect(Collectors.toList());
//    }

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

        if(currentUserHasPrivilege(PrivilegeType.FULL_ACCESS)) {
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
