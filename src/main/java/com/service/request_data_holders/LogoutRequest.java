package com.service.request_data_holders;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogoutRequest {

    private String username;

    public LogoutRequest(@JsonProperty("username") String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
