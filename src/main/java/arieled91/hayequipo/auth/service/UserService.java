package arieled91.hayequipo.auth.service;

import arieled91.hayequipo.auth.exception.UserAlreadyExistsException;
import arieled91.hayequipo.auth.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerNewUserAccount(User user) throws UserAlreadyExistsException;

    void saveRegisteredUser(User user);

    void deleteUser(User user);

    User findUserByEmail(String email);

    Optional<User> getUserById(long id);

    void changeUserPassword(User user, String password);

    boolean checkIfValidOldPassword(User user, String password);

    List<String> getUsersFromSessionRegistry();

//    void createVerificationTokenForUser(User user, String token);

//    void createPasswordResetTokenForUser(User user, String token);

//    User getUser(String verificationToken);

//    PasswordResetToken getPasswordResetToken(String token);

//    User getUserByPasswordResetToken(String token);


//    String validateVerificationToken(String token);

//    User updateUser2FA(boolean use2FA);


}
