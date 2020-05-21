package com.service.request_data_holders;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TeamRequest {

    private String ownerUserName;
    private String teamName;
    private String fieldCountry;
    private String fieldState;
    private String fieldCity;
    private String fieldPostalCode;



    public TeamRequest(@JsonProperty("ownerUserName") String ownerUserName, @JsonProperty("teamName") String teamName, @JsonProperty("fieldCountry")String fieldCountry, @JsonProperty("fieldState") String fieldState, @JsonProperty("fieldCity")String fieldCity, @JsonProperty("fieldPostalCode")String fieldPostalCode) {
        this.ownerUserName = ownerUserName;
        this.teamName = teamName;
        this.fieldCountry = fieldCountry;
        this.fieldState=fieldState;
        this.fieldCity = fieldCity;
        this.fieldPostalCode = fieldPostalCode;
    }

    public String getOwnerUserName() {
        return ownerUserName;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getFieldCountry() {
        return fieldCountry;
    }

    public String getFieldState() {
        return fieldState;
    }

    public String getFieldCity() {
        return fieldCity;
    }

    public String getFieldPostalCode() {
        return fieldPostalCode;
    }
}
