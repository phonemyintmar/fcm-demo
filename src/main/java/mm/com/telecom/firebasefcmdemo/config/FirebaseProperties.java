package mm.com.telecom.firebasefcmdemo.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"file:./config/firebase-${spring.profiles.active}.properties"})
//a mhan ka d project mhr so $spring.profiles.active so tr ka m lo wo tutu pl but use tat ag htae htr tr
//file ka tutu pl so firebase.properties pl lote loh ya dl
@ConfigurationProperties(prefix = "firebase.config")
@Getter
@Setter
public class FirebaseProperties {

    private String firebaseAPIKey;
    private String firebaseFCMSendURL;
    private String firebaseDeviceGroupUrl;
    private String firebaseProjectId;
}
