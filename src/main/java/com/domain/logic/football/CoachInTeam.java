package com.domain.logic.football;


import com.domain.logic.roles.Coach;

public class CoachInTeam {


   private String description;
   private Coach coach;
   private Team team;

    public CoachInTeam(String description, Coach coach, Team team) {
        this.description = description;
        this.coach = coach;
        this.team = team;
    }

    public CoachInTeam(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Coach getCoach() {
        return coach;
    }

    public Team getTeam() {
        return team;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
