package com.stubs;


import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.CoachInTeam;
import com.domain.logic.football.Field;
import com.domain.logic.football.PlayerRoleInTeam;
import com.domain.logic.football.Team;
import com.domain.logic.roles.TeamManager;
import com.domain.logic.roles.TeamOwner;

import java.util.ArrayList;

public class TeamStub extends Team {
    public TeamStatus teamStatus = TeamStatus.Open;
    public ArrayList<TeamOwner> teamOwners;
    public ArrayList<TeamManager> teamManagers;
    public ArrayList<CoachInTeam> coaches;
    public ArrayList<PlayerRoleInTeam> players;

    public TeamStub(String teamName, TeamStatus status, TeamOwner team_owner, Field field) {
        super(teamName, status, team_owner, field);
        this.teamOwners = new ArrayList<>();
        this.teamManagers = new ArrayList<>();
        this.coaches = new ArrayList<>();
        this.players = new ArrayList<>();
    }

    @Override
    public TeamStatus getStatus() {
        return teamStatus;
    }

    @Override
    public ArrayList<TeamOwner> getTeam_owners() {
        return this.teamOwners;
    }

    @Override
    public ArrayList<TeamManager> getTeam_managers(){
        return this.teamManagers;
    }

    @Override
    public ArrayList<CoachInTeam> getCoaches() {
        return this.coaches;
    }

    @Override
    public void addCoachInTeam(CoachInTeam coachInTeam){
        this.coaches.add(coachInTeam);
    }

    @Override
    public ArrayList<PlayerRoleInTeam> getPlayers() {
        return this.players;
    }

    @Override
    public void addPlayerRoleInTeam(PlayerRoleInTeam playerRoleInTeam){
        this.players.add(playerRoleInTeam);
    }
}
