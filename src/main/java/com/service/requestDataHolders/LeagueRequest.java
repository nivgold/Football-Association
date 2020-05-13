package com.service.requestDataHolders;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LeagueRequest {

    private String leagueName;


    public LeagueRequest (@JsonProperty("leagueName") String leagueName) {
        this.leagueName = leagueName;
        //TODO call DAO to add league
    }
}
