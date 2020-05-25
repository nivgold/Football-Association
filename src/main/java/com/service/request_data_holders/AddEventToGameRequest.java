package com.service.request_data_holders;


import com.fasterxml.jackson.annotation.JsonProperty;

public class AddEventToGameRequest {

    private String refereeUsername;
    private int gameID;
    int gameMinute;
    String description;
    String type;
    int playerID;

    public AddEventToGameRequest(@JsonProperty("refereeUsername")String refereeUsername, @JsonProperty("gameID")int gameID, @JsonProperty("gameMinute")int gameMinute, @JsonProperty("description")String description, @JsonProperty("type")String type, @JsonProperty("playerID")int playerID) {
        this.refereeUsername = refereeUsername;
        this.gameID = gameID;
        this.gameMinute = gameMinute;
        this.description = description;
        this.type = type;
        this.playerID = playerID;
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

    public int getPlayerID() {
        return playerID;
    }
}
