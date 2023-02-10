package mm.com.telecom.firebasefcmdemo.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import lombok.extern.slf4j.Slf4j;
import mm.com.telecom.firebasefcmdemo.config.FirebaseProperties;
import mm.com.telecom.firebasefcmdemo.dto.*;
import mm.com.telecom.firebasefcmdemo.util.PMUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class TestService {
    private final FirebaseMessaging firebaseMessaging;

    private final FirebaseProperties firebaseProperties;


    public TestService(FirebaseMessaging firebaseMessaging, FirebaseProperties firebaseProperties) {
        this.firebaseMessaging = firebaseMessaging;
        this.firebaseProperties = firebaseProperties;
    }

    public void sendNotification(SendNotificationDto sendNotificationDto) {

        Map<String, Object> body = new HashMap<>();
        body.put("to", sendNotificationDto.getTo());
        body.put("priority", "high");

        Map<String, String> notification = new HashMap<>();
        notification.put("title", sendNotificationDto.getTitle());
        notification.put("body", sendNotificationDto.getMessage());
        notification.put("image", sendNotificationDto.getImgUrl());

        body.put("notification", notification);
        log.info("final request body {}", body);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, getHeaders());

        RestTemplate restTemplate = PMUtils.getProxiedTemplate();
        String firebaseResponse = restTemplate.postForObject(firebaseProperties.getFirebaseFCMSendURL(), request, String.class);

        log.info("Firebase Response : " + firebaseResponse);
    }


    public ResponseEntity<?> subscribeToTopic(TopicSubscriptionDto dto) throws FirebaseMessagingException {

        try {
            TopicManagementResponse topicResponse = firebaseMessaging.subscribeToTopic(
                    List.of(dto.getRegistrationToken()), dto.getTopic());
            Map<String, String> response = new HashMap<>();
            response.put("successTokens", String.valueOf(topicResponse.getSuccessCount()));
            return ResponseEntity.ok().body(response);
        } catch (FirebaseMessagingException e) {
            log.error("error in subscribing to the topic {} with the error message : {}", dto.getTopic(), e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }

    public ResponseEntity<?> createDeviceGroup(CreateDeviceGroupDto dto) {
        DeviceGroupDto deviceGroupDto = new DeviceGroupDto();
        deviceGroupDto.setOperation("create");
        deviceGroupDto.setNotification_key_name(dto.getDeviceGroupName());
        deviceGroupDto.setRegistration_ids(dto.getMemberList());

        HttpHeaders headers = getHeaders();
        headers.set("project_id", firebaseProperties.getFirebaseProjectId());
        HttpEntity<DeviceGroupDto> request = new HttpEntity<>(deviceGroupDto, headers);

        RestTemplate restTemplate = PMUtils.getProxiedTemplate();
        try {
            String firebaseResponse = restTemplate.postForObject(firebaseProperties.getFirebaseDeviceGroupUrl(), request, String.class);
            log.info("Firebase Response : " + firebaseResponse);
            //to object helper lote pee ae tr nae return pyan
            return ResponseEntity.ok(firebaseResponse);
        } catch (Exception e) {
            log.error("error in creating device group for the group {} with the message : {}", dto.getDeviceGroupName(), e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }

    }

    //dr ko add or remove lote pee
    //add or remove so tae enum pass khine lite ae tr po a sin pyay tl
    public ResponseEntity<?> addToDeviceGroup(AddOrRemoveDeviceGroupDto dto) {
        DeviceGroupDto deviceGroupDto = new DeviceGroupDto();
        deviceGroupDto.setOperation("add");
        deviceGroupDto.setNotification_key_name(dto.getDeviceGroupName());
        deviceGroupDto.setNotification_key(dto.getDeviceGroupKey());
        deviceGroupDto.setRegistration_ids(dto.getMemberList());

        HttpHeaders headers = getHeaders();
        headers.set("project_id", firebaseProperties.getFirebaseProjectId());
        HttpEntity<DeviceGroupDto> request = new HttpEntity<>(deviceGroupDto, headers);

        RestTemplate restTemplate = PMUtils.getProxiedTemplate();

        try {
            String firebaseResponse = restTemplate.postForObject(firebaseProperties.getFirebaseDeviceGroupUrl(), request, String.class);
            log.info("Firebase Response : " + firebaseResponse);
            //to object helper lote pee ae tr nae return pyan
            return ResponseEntity.ok(firebaseResponse);
        } catch (Exception e) {
            log.error("error in adding devices {} to the device group with the message : {}", dto.getMemberList(), e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    public ResponseEntity<?> getNotificationKey(String name) {
        HttpHeaders headers = getHeaders();
        headers.set("project_id", firebaseProperties.getFirebaseProjectId());
        RestTemplate restTemplate = PMUtils.getProxiedTemplate();

        log.info("headers {}", headers);

        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("notification_key_name", name);
        try {
            HttpEntity<Object> request = new HttpEntity<>(null, headers);
            ResponseEntity<String> firebaseResponse = restTemplate.exchange(firebaseProperties.getFirebaseDeviceGroupUrl() + "?notification_key_name=" + name, HttpMethod.GET, request, String.class);

//            log.info("aaa",re);
            return ResponseEntity.ok(firebaseResponse.getBody());
        } catch (Exception e) {
            log.error("error in getting notificationKey with the message : {}", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "key=" + firebaseProperties.getFirebaseAPIKey());
        return httpHeaders;
    }
}

