package com.service.request_data_holders;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameSettingPolicyRequest {

    private String associationAgentUsername;
    private String leagueName;
    private int seasonYear;
    private String gameSettingPolicy;

    public GameSettingPolicyRequest(@JsonProperty("associationAgentUsername") String associationAgentUsername, @JsonProperty("leagueName") String leagueName, @JsonProperty("seasonYear") int seasonYear, @JsonProperty("gameSettingPolicy") String gameSettingPolicy) {
        this.associationAgentUsername = associationAgentUsername;
        this.leagueName = leagueName;
        this.seasonYear = seasonYear;
        this.gameSettingPolicy = gameSettingPolicy;
    }

    public String getAssociationAgentUsername() {
        return associationAgentUsername;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public int getSeasonYear() {
        return seasonYear;
    }

    public String getGameSettingPolicy() {
        return gameSettingPolicy;
    }
}