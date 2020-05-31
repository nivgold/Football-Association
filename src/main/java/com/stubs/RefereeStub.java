package com.stubs;

import com.data.DBCommunicator;
import com.data.Dao;
import com.domain.logic.enums.EventType;
import com.domain.logic.football.Event;
import com.domain.logic.football.Game;
import com.domain.logic.roles.Referee;
import com.domain.logic.users.Member;

public class RefereeStub extends Referee {

    private String username;
    public RefereeStub(Member member) throws Exception {
        super(member);
        this.username = member.getUserName();
    }

    @Override
    public Event createGameEvent(int gameMinute, String description, EventType type, int gameID, String hostTeamName, String guestTeamName, String playerUsername) throws Exception {
        if (isAuthorized(gameID)){
            // 0 - no score change
            // 1 - add goal to host team
            // -1 - add goal to guest team
            int changeScore = 0;
            if (type == EventType.HostGoal){
                changeScore = 1;
            }
            else if (type == EventType.GuestGoal){
                changeScore = -1;
            }
            Dao dao = DBStub.getInstance();
            dao.addGameEvent(gameID, gameMinute, description, type, playerUsername, changeScore);
            return new Event(gameMinute, description, type, gameID, hostTeamName, guestTeamName, playerUsername);
        }
        else{
            throw new Exception("\""+this.username+"\" is not authorized to add game event in gameID: "+gameID);
        }
    }

    private boolean isAuthorized(int gameID){
        Dao dao = DBStub.getInstance();
        if(dao.findGame(gameID) != null)
            return true;
        return false;
    }

}
