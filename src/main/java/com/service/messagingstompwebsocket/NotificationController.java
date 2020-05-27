package com.service.messagingstompwebsocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.SplittableRandom;

@Controller
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate simpleMessagingTemplate;

    @MessageMapping("/hello")
    public void greeting(int gameID, NotificationMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        simpleMessagingTemplate.convertAndSend("topic/games/" + gameID, message);

        //return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");

    }
}
