package mm.com.telecom.firebasefcmdemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicSubscriptionDto {
    private String topic;
    private String registrationToken; //fcmToken
}
