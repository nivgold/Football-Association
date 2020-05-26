package com.service.request_data_holders;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.RequestBody;

public class CreateReportRequest {

    String refereeUsername;
    int gameID;
    String report;

    public CreateReportRequest(@JsonProperty("refereeUsername") String refereeUsername, @JsonProperty("gameID") int gameID, @JsonProperty("report") String report) {
        this.refereeUsername = refereeUsername;
        this.gameID = gameID;
        this.report=report;
    }

    public String getRefereeUsername() {
        return refereeUsername;
    }

    public int getGameID() {
        return gameID;
    }

    public String getReport() {
        return report;
    }
}
