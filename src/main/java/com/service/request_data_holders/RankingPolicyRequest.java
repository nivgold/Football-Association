package com.service.request_data_holders;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RankingPolicyRequest {

    private String associationAgentUsername;
    private String leagueName;
    private int seasonYear;
    private int win;
    private int goal;
    private int draw;
    private int yellowCards;
    private int redCards;

    public RankingPolicyRequest(@JsonProperty("") String associationAgentUsername, String leagueName, int seasonYear, int win, int goal, int draw, int yellowCards, int redCards) {
        this.associationAgentUsername = associationAgentUsername;
        this.leagueName = leagueName;
        this.seasonYear = seasonYear;
        this.win = win;
        this.goal = goal;
        this.draw = draw;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
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

    public int getWin() {
        return win;
    }

    public int getGoal() {
        return goal;
    }

    public int getDraw() {
        return draw;
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public int getRedCards() {
        return redCards;
    }
}
