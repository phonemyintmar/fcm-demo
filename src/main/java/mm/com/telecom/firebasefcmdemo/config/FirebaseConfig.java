package mm.com.telecom.firebasefcmdemo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@Slf4j
public class FirebaseConfig {

    private final AppConfig appConfig;

    public FirebaseConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }


    @Bean
    GoogleCredentials googleCredentials() throws IOException {
        log.info("path : {}",appConfig.getCredentialsFilePath());
        File credentialsFile = new File(appConfig.getCredentialsFilePath());
        if (credentialsFile.exists()) {
            try (InputStream is = new FileInputStream(credentialsFile)) {
                return GoogleCredentials.fromStream(is);
            }
        } else {
            log.info("ho htl ma yout wo");
            // Use standard credentials chain. Useful when running inside GKE
            return GoogleCredentials.getApplicationDefault();
        }

    }

    @Bean
    FirebaseApp firebaseApp(GoogleCredentials credentials) {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
