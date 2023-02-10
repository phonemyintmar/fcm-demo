package mm.com.telecom.firebasefcmdemo.controller;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import mm.com.telecom.firebasefcmdemo.dto.*;
import mm.com.telecom.firebasefcmdemo.service.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin
@Slf4j
public class TestController {

    private final TestService testService;

    private final FirebaseMessaging fcm;

    public TestController(TestService testService, FirebaseMessaging fcm) {
        this.testService = testService;
        this.fcm = fcm;
    }


    //out ka lo myo messaging lote pee tok set topic nay yr mhr set token lote ll ya dl
    @PostMapping("send-noti/{token}")
    public ResponseEntity<?> send(@PathVariable String token) {
        SendNotificationDto sendNotificationDto = new SendNotificationDto();
        sendNotificationDto.setType(TargetType.SINGLE_DEVICE);
        sendNotificationDto.setTo(token);
        sendNotificationDto.setTitle("Test Title");
        sendNotificationDto.setMessage("Hello World");
        sendNotificationDto.setImgUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ60oQyKV5jdNH1Kq1vL8OV-Lc6xUjhikNB3Q&usqp=CAU");
        testService.sendNotification(sendNotificationDto);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("completeAt", LocalDateTime.now().toString());
        return ResponseEntity.ok().body(responseMap);
    }


    @PostMapping("send-msg-topic/{topic}")
    public ResponseEntity<?> sendmsg(@PathVariable String topic) throws FirebaseMessagingException {

        //dr ka a pw ka send noti ka a tine ko pl fcmToken nay yr mhr
        //"to" param ko topics/"topicName" so pee send loh ll ya dl
        Message msg = Message.builder()
                .setTopic(topic)
                .setNotification(Notification.builder()
                        .setTitle("test title")
                        .setBody("test body")
//                        .setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHVsus866Ccf_8-VBIXVdL0Y-qqy68AWK8nQ&usqp=CAU")
                        .setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ60oQyKV5jdNH1Kq1vL8OV-Lc6xUjhikNB3Q&usqp=CAU")
                        .build())
                .build();
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("firebaseResponse", fcm.send(msg));

        //d lo firebase sdk messaging nae tann send yin wifi lo dl
        //proxy ka java or jvm mhr thwr tann khan ya dl
        //and device group a twet ka d lo sdk nae ma shi wo
        return ResponseEntity.ok(responseMap);
    }

    //unsubscribe ll lote loh ya dl
    @PostMapping("subscribe-topic")
    public ResponseEntity<?> subscribeToTopic(@RequestBody TopicSubscriptionDto dto) throws FirebaseMessagingException {
        return testService.subscribeToTopic(dto);
    }

    @PostMapping("device-group/create")
    public ResponseEntity<?> createDeviceGroup(@RequestBody CreateDeviceGroupDto dto) throws FirebaseMessagingException {
        return testService.createDeviceGroup(dto);
    }

    @PostMapping("device-group/add")
    public ResponseEntity<?> addToDeviceGroup(@RequestBody AddOrRemoveDeviceGroupDto dto) {
        return testService.addToDeviceGroup(dto);
    }

    //dr myo ko topic a twet ll unsubscribe lote loh ya dl
    //a pw ka nae tutu pl operation mhr remove lite yone pl
    @PostMapping("device-group/remove")
    public ResponseEntity<?> removeFromDeviceGroup() {
        return null;
    }

    @PostMapping("device-group/send")
    public ResponseEntity<?> sendToDeviceGroup(@RequestParam String id){
        // dr twy ko response ko kgkg pyan ag lote
        SendNotificationDto sendNotificationDto = new SendNotificationDto();
        sendNotificationDto.setType(TargetType.DEVICE_GROUP);
        sendNotificationDto.setTo(id);
        sendNotificationDto.setTitle("Test Title");
        sendNotificationDto.setMessage("Hello World");
        sendNotificationDto.setImgUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ60oQyKV5jdNH1Kq1vL8OV-Lc6xUjhikNB3Q&usqp=CAU");
        testService.sendNotification(sendNotificationDto);
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("completeAt", LocalDateTime.now().toString());
        return ResponseEntity.ok().body(responseMap);
    }

    @GetMapping("device-group/name/get")
    public ResponseEntity<?> getDeviceGroupKey(@RequestParam String name) {
        //dr ka ma yay chin tok wo
        //retrieving notification key so pee tok shr kyi android phyt phyt web phyt phyt tutu pl
        //pisi lay lwl lwl layy
        // notificaiton api a nout mhr request param nae notification_key_name= so pee tok value htae lite yin return pyan tl. get method pl
        //yayy ml yayy ml
       return testService.getNotificationKey(name);
    }


}
