package co.grtk.oauth.client.event.listener;

import co.grtk.oauth.client.entity.WebUser;
import co.grtk.oauth.client.event.RegistrationCompleteEvent;
import co.grtk.oauth.client.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@AllArgsConstructor
@Component
@Slf4j
public class RegistrationCompleteEventListener implements
        ApplicationListener<RegistrationCompleteEvent> {

    private final UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //Create the Verification Token for the User with Link
        WebUser webUser = event.getWebUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, webUser);
        //Send Mail to user
        String url =
                event.getApplicationUrl()
                        + "/verifyRegistration?token="
                        + token;

        //sendVerificationEmail()
        log.info("Click the link to verify your account: {}",
                url);
    }
}
