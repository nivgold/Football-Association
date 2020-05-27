package com.service.messagingstompwebsocket;

public class NotificationMessage {
    private String content;

    public NotificationMessage() {
    }

    public NotificationMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
