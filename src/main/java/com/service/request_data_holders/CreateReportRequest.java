package com.service.request_data_holders;

import org.springframework.web.bind.annotation.RequestBody;

public class CreateReportRequest {

    String refereeUsername;
    int gameID;

    public CreateReportRequest(@RequestBody String refereeUsername, @RequestBody int gameID) {
        this.refereeUsername = refereeUsername;
        this.gameID = gameID;
    }

    public String getRefereeUsername() {
        return refereeUsername;
    }

    public int getGameID() {
        return gameID;
    }
}
