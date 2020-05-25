package com.service;

public class StatusResponse {

    String status;

    public StatusResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static StatusResponse getTrueStatusObj() {
        return new StatusResponse("true");
    }

    public static StatusResponse getFalseStatusObj() {
        return new StatusResponse("false");
    }
}
