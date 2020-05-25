package com.stubs;

import com.data.DBCommunicator;
import com.data.Dao;
import com.domain.logic.data_types.Address;
import com.domain.logic.football.Field;
import com.domain.logic.football.Team;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;

public class TeamOwnerStub extends TeamOwner {

    private Member member;
    private Team team;

    public TeamOwnerStub(Member member) throws Exception {
        super(member);
        this.member = member;
        this.team = null;
    }

    public void createTeam(String teamName, Field field) throws Exception {
        if (team!=null)
            throw new Exception("team owner already has team");
        Dao dao = DBStub.getInstance();
        if (!dao.checkIfTeamExists(teamName)){
            Address fieldAddress = field.getLocation();
            dao.addTeam(teamName, fieldAddress.getCountry(), fieldAddress.getState(), fieldAddress.getCity(), fieldAddress.getPostalCode(), this);
        }
        else{
            throw new Exception("team name already exists in the DB");
        }
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
