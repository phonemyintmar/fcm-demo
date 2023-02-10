package mm.com.telecom.firebasefcmdemo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
//request DTO
//real project twy mhr so request reponse dto twy ko file khwl yin khwl pop
public class CreateDeviceGroupDto {
    private String deviceGroupName;
    private List<String> memberList; //fcmTokenList
}
