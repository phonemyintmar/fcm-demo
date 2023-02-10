package mm.com.telecom.firebasefcmdemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendNotificationDto {
    private String to;
    private String title;
    private String message;
    private TargetType type;
    private String imgUrl;
}
