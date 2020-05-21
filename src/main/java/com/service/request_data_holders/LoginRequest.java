package com.service.request_data_holders;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {

    String userName;
    String password;

    public LoginRequest(@JsonProperty String userName, @JsonProperty String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
