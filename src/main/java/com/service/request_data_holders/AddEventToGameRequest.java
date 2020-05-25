package com.service.request_data_holders;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class AddEventToGameRequest {

    String refereeUsername;
    int gameID;
    int gameMinute;
    String description;
    String type;
    String playerUsername;

    public AddEventToGameRequest(@JsonProperty("refereeUsername")String refereeUsername, @JsonProperty("gameID")int gameID, @JsonProperty("gameMinute")int gameMinute, @JsonProperty("description")String description, @JsonProperty("type")String type, @JsonProperty("PlayerUsername")String playerUsername) {
        this.refereeUsername = refereeUsername;
        this.gameID = gameID;
        this.gameMinute = gameMinute;
        this.description = description;
        this.type = type;
        this.playerUsername = playerUsername;
    }

    public String getRefereeUsername() {
        return refereeUsername;
    }

    public int getGameID() {
        return gameID;
    }

    public int getGameMinute() {
        return gameMinute;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getPlayerUsername() {
        return playerUsername;
    }
}
