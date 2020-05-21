package com.service.responses;

public class LoginResponse {

    String userName;
    String status;

    public LoginResponse(String userName, String succeeded) {
        this.userName = userName;
        this.status = succeeded;
    }

    public String getUserName() {
        return userName;
    }

    public String getSucceeded() {
        return status;
    }
}
