package co.grtk.oauth.client.service;

import co.grtk.oauth.client.entity.PasswordResetToken;
import co.grtk.oauth.client.entity.VerificationToken;
import co.grtk.oauth.client.entity.WebUser;
import co.grtk.oauth.client.model.UserModel;
import co.grtk.oauth.client.repository.PasswordResetTokenRepository;
import co.grtk.oauth.client.repository.UserRepository;
import co.grtk.oauth.client.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public WebUser registerUser(UserModel userModel) {
        WebUser webUser = new WebUser();
        webUser.setEmail(userModel.getEmail());
        webUser.setFirstName(userModel.getFirstName());
        webUser.setLastName(userModel.getLastName());
        webUser.setRole("USER");
        webUser.setPassword(passwordEncoder.encode(userModel.getPassword()));

        userRepository.save(webUser);
        return webUser;
    }

    @Override
    public void saveVerificationTokenForUser(String token, WebUser webUser) {
        VerificationToken verificationToken
                = new VerificationToken(webUser, token);

        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken
                = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            return "invalid";
        }

        WebUser webUser = verificationToken.getWebUser();
        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }

        webUser.setEnabled(true);
        userRepository.save(webUser);
        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken
                = verificationTokenRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public WebUser findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(WebUser webUser, String token) {
        PasswordResetToken passwordResetToken
                = new PasswordResetToken(webUser,token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken
                = passwordResetTokenRepository.findByToken(token);

        if (passwordResetToken == null) {
            return "invalid";
        }

        WebUser webUser = passwordResetToken.getWebUser();
        Calendar cal = Calendar.getInstance();

        if ((passwordResetToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";
        }

        return "valid";
    }

    @Override
    public Optional<WebUser> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getWebUser());
    }

    @Override
    public void changePassword(WebUser webUser, String newPassword) {
        webUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(webUser);
    }

    @Override
    public boolean checkIfValidOldPassword(WebUser webUser, String oldPassword) {
        return passwordEncoder.matches(oldPassword, webUser.getPassword());
    }
}
