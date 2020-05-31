package com.service.messagingstompwebsocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;

public class Notification {

    private static final String destinationPrefix = "/game";

    public static void sendGameEventNotification(SimpMessagingTemplate simpMessagingTemplate, int gameID, String message){
        String destination = destinationPrefix+"/"+gameID;
        try {
            simpMessagingTemplate.convertAndSend(destination, new NotificationMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
