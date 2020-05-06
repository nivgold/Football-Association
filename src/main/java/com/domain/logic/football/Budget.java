package com.domain.logic.football;

public class Budget {
    private Season season;
    private Team team;
    private double amount;

    public Budget(Season season, Team team) {
        this.season = season;
        this.team = team;
        this.amount = 0;
    }

    public Budget(Season season, Team team, double amount) {
        this.season = season;
        this.team = team;
        this.amount = amount;
    }

    public Season getSeason() {
        return season;
    }

    public Team getTeam() {
        return team;
    }

    public double getAmount() {
        return amount;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void increaseAmount(double amount){
        this.amount += amount;
    }

    public void decreaseAmount(double amount){
        this.amount -= amount;
    }
}
