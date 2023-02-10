package mm.com.telecom.firebasefcmdemo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeviceGroupDto {
    private String operation;
    private String notification_key_name;
    private List<String> registration_ids;
    private String notification_key;
}
