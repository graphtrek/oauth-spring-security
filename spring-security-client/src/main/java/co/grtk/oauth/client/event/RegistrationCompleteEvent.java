package co.grtk.oauth.client.event;

import co.grtk.oauth.client.entity.WebUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private final WebUser webUser;
    private final String applicationUrl;

    public RegistrationCompleteEvent(WebUser webUser, String applicationUrl) {
        super(webUser);
        this.webUser = webUser;
        this.applicationUrl = applicationUrl;
    }
}
