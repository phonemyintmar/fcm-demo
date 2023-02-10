package mm.com.telecom.firebasefcmdemo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
//request Dto
public class AddOrRemoveDeviceGroupDto {
    private String deviceGroupKey;
    private String deviceGroupName;
    private List<String> memberList;
}
