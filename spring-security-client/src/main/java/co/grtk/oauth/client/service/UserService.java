package co.grtk.oauth.client.service;

import co.grtk.oauth.client.entity.WebUser;
import co.grtk.oauth.client.entity.VerificationToken;
import co.grtk.oauth.client.model.UserModel;

import java.util.Optional;

public interface UserService {
    WebUser registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, WebUser webUser);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);

    WebUser findUserByEmail(String email);

    void createPasswordResetTokenForUser(WebUser webUser, String token);

    String validatePasswordResetToken(String token);

    Optional<WebUser> getUserByPasswordResetToken(String token);

    void changePassword(WebUser webUser, String newPassword);

    boolean checkIfValidOldPassword(WebUser webUser, String oldPassword);
}
