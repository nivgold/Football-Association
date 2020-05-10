package com.domain.logic.roles;

import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.Team;
import com.domain.logic.users.Member;
import com.logger.Logger;

public class TeamManager implements IRole, ITeamObserver {

    private Team team;
    private TeamOwner appointer;
    private Member member;

    public TeamManager(Team team, Member member, TeamOwner appointer) {
        this.team = team;
        this.member = member;
        this.appointer = appointer;
        member.addTeamManager(this);
    }

    /**
     * remove the object from all occurrences
     * @return
     */
    @Override
    public boolean removeYourself() {
        try {
            team.getTeam_managers().remove(this);
            member.removeTeamManager(this);
            this.appointer.getAppointments().remove(this);
            this.team = null;
            this.appointer=null;
            Logger.getInstance().saveLog("The team manager has been removed successfully");
            return true;
        }
        catch (Exception e) {
            e.getStackTrace();
        }
        Logger.getInstance().saveLog("An error occurred - couldn't remove the team manager");
        return false;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public TeamOwner getAppointer() {
        return appointer;
    }

    public void setAppointer(TeamOwner appointer) {
        this.appointer = appointer;
    }

    @Override
    public void teamUpdate(TeamStatus ts) {
        System.out.println("The team status has been changed to " + ts.toString());
    }

    public void registerToTeamStatus(){
        this.team.register(this);
    }

    public void removeFromTeamStatus(){
        this.team.remove(this);
    }


}
