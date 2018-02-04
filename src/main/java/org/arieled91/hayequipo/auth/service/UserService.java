package org.arieled91.hayequipo.auth.service;

import org.arieled91.hayequipo.auth.exception.UserAlreadyExistsException;
import org.arieled91.hayequipo.auth.model.User;
import org.arieled91.hayequipo.auth.model.VerificationToken;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerNewUserAccount(User user) throws UserAlreadyExistsException;

    void saveRegisteredUser(User user);

    void deleteUser(User user);

    Optional<User> findActiveUserByMail(String email);

    Optional<User> getUserById(long id);

    void changeUserPassword(User user, String password);

    boolean checkIfValidOldPassword(User user, String password);

    List<String> getUsersFromSessionRegistry();

    void createVerificationTokenForUser(User user, String token);

    VerificationToken generateNewVerificationToken(String token);

//    void createPasswordResetTokenForUser(User user, String token);

    User getUser(String verificationToken);


    VerificationToken getVerificationToken(String VerificationToken);

//    PasswordResetToken getPasswordResetToken(String token);

//    User getUserByPasswordResetToken(String token);


    String validateVerificationToken(String token);

//    User updateUser2FA(boolean use2FA);

    String TOKEN_INVALID = "invalidToken";
    String TOKEN_EXPIRED = "expired";
    String TOKEN_VALID = "valid";


}
