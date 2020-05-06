package com.stubs;


import com.domain.logic.football.CoachInTeam;
import com.domain.logic.football.Team;
import com.domain.logic.roles.Coach;

public class CoachInTeamStub extends CoachInTeam {

    private String description;
    private Coach coach;
    private Team team;

    public CoachInTeamStub(String description, Coach coach, Team team) {
        super(description, coach, team);
        this.description = description;
        this.coach = coach;
        this.team = team;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Coach getCoach() {
        return coach;
    }

    @Override
    public Team getTeam() {
        return team;
    }
}
