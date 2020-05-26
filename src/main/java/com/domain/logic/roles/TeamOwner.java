package com.domain.logic.roles;

import com.data.DBCommunicator;
import com.data.Dao;
import com.domain.logic.data_types.Address;
import com.domain.logic.enums.PlayerRole;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.CoachInTeam;
import com.domain.logic.football.Field;
import com.domain.logic.football.PlayerRoleInTeam;
import com.domain.logic.football.Team;
import com.domain.logic.users.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logger.EventLogger;

import java.util.ArrayList;

public class TeamOwner implements IRole, ITeamObserver {

    private Team team;
    private TeamOwner appointer;
    private ArrayList<IRole> appointments;
    private Member member;

    public TeamOwner(Member member, String teamName, TeamStatus teamStatus, Field field) throws Exception {
        this.member = member;
        this.appointments = new ArrayList<>();
        this.team = new Team(teamName, teamStatus, this, field);
        member.addTeamOwner(this);
    }

    public void createTeam(String teamName, Field field) throws Exception {
        if (team != null)
            throw new Exception("team owner already has team");
        Dao dao = DBCommunicator.getInstance();
        if(dao.hasTeam(this.member.getUserName()))
            throw new Exception("team owner already has team");
        if (!dao.checkIfTeamExists(teamName)){
            Address fieldAddress = field.getLocation();
            dao.addTeam(teamName, fieldAddress.getCountry(), fieldAddress.getState(), fieldAddress.getCity(), fieldAddress.getPostalCode(), this);
        }
        else{
            throw new Exception("team name already exists in the DB");
        }
    }

    public TeamOwner(Member member) throws Exception {
        this.member = member;
        this.appointments = new ArrayList<>();
        member.addTeamOwner(this);
        //TODO call the DAO to add new team owner to the DB
    }

    public TeamOwner(Team team, Member member) throws Exception {
        this.team = team;
        this.member = member;
        this.appointments = new ArrayList<>();
        member.addTeamOwner(this);
        //TODO call the DAO to add new team owner to the DB
    }

    /**
     * adds new team owner to the team
     *
     * @param m
     */
    public void appointTeamOwner(Member m) throws Exception {
        if (team.getStatus().equals(TeamStatus.Open)) {
            boolean found = false;
            for (IRole role : m.getRoles()) {
                if (role.getClass() == TeamOwner.class || role.getClass() == TeamManager.class) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                TeamOwner to = new TeamOwner(team, m);
                to.setAppointer(this);
                this.appointments.add(to);
                team.getTeam_owners().add(to);
                EventLogger.getInstance().saveLog("The new team owner has been added successfully");
            } else {
                EventLogger.getInstance().saveLog("the member is already a team owner");
            }
        } else {
            EventLogger.getInstance().saveLog("The team status is closed, you can't do any operations");
        }

    }

    /**
     * removes team owner
     *
     * @param to
     */
    public void removeTeamOwner(TeamOwner to) throws Exception {
        if (team.getStatus().equals(TeamStatus.Open)) {
            boolean authorized = isAuthorized(to);
            if (authorized) {
                for (IRole r : to.appointments) {
                    team.getTeam_owners().remove(r);
                    r.removeYourself();
                }
                appointments.remove(to);
                to.removeYourself();
            } else {
                EventLogger.getInstance().saveLog("You don't have the authority to remove this team owner");
            }
        } else {
            EventLogger.getInstance().saveLog("The team status is closed, you can't do any operations");
        }
    }

    private boolean isAuthorized(TeamOwner to) {
        if (appointments.contains(to)) {
            return true;
        }

        for (IRole role : this.appointments) {
            if (role instanceof TeamOwner) {
                ((TeamOwner) role).isAuthorized(to);
            }
        }
        return false;
    }

    /**
     * adds new team manager to the team
     *
     * @param m
     */
    public void appointTeamManager(Member m) throws Exception {
        if (team.getStatus().equals(TeamStatus.Open)) {
            boolean valid = true;
            for (IRole role : m.getRoles()) {
                if (role.getClass() == TeamOwner.class || role.getClass() == TeamManager.class) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                TeamManager tm = new TeamManager(team, m, this);
                team.getTeam_managers().add(tm);
                appointments.add(tm);
                EventLogger.getInstance().saveLog("The team manager has been added successfully");
            } else {
                EventLogger.getInstance().saveLog("This member is already owner/manager of this team");
            }
        } else {
            EventLogger.getInstance().saveLog("The team status is closed, you can't do any operations");
        }
    }

    /**
     * remove team manager from the team
     *
     * @param tm
     */
    public void removeTeamManager(TeamManager tm) throws Exception {
        if (team.getStatus().equals(TeamStatus.Open)) {
            if (tm.getAppointer() == this) {
                tm.removeYourself();
            } else {
                EventLogger.getInstance().saveLog("You don't have the authority to remove this team manager");
            }
        } else {
            EventLogger.getInstance().saveLog("The team status is closed, you can't do any operations");
        }
    }


    /**
     * set team status to 'close'
     */
    public void closeTeam() {
        team.setStatus(TeamStatus.Closed);
        EventLogger.getInstance().saveLog("The team status changed to 'Closed'");
    }

    /**
     * set team status to 'open'
     */
    public void openTeam() {
        team.setStatus(TeamStatus.Open);
        EventLogger.getInstance().saveLog("The team status changed to 'Open'");
    }

    /**
     * creates new financial report for the team
     */
    public void createFinancialReport(String report) {
        //todo add String financialReport to team fields
        //team.setFinancialReport(report);
    }

    /**
     * appoints new coach to the team
     *
     * @param c
     */
    public void appointCoach(Coach c) {
        if (team.getStatus().equals(TeamStatus.Open)) {
            CoachInTeam cit = new CoachInTeam("New coach", c, this.team);
            c.addCoachInTeam(cit);
            team.addCoachInTeam(cit);
            EventLogger.getInstance().saveLog("The coach has been added successfully");
        } else {
            EventLogger.getInstance().saveLog("The team status is closed, you can't do any operations");
        }
    }

    public void removeCoach(Coach c) throws Exception {
        if (team.getStatus().equals(TeamStatus.Open)) {
            boolean exists = false;
            CoachInTeam toRemove = null;
            for (CoachInTeam cit : team.getCoaches()) {
                if (cit.getCoach().getMember().equals(c.getMember())) {
                    exists = true;
                    toRemove = cit;
                }
            }
            if (exists) {
                team.getCoaches().remove(toRemove);
                c.getTeams().remove(toRemove);
                if (c.getTeams().size() == 0) {
                    c.getMember().removeCoach(c);
                }
                EventLogger.getInstance().saveLog("The coach has been removed successfully");
            } else {
                EventLogger.getInstance().saveLog("This coach is not part of the team's coaches");
            }
        } else {
            EventLogger.getInstance().saveLog("The team status is closed, you can't do any operations");
        }
    }

    /**
     * add new player to the team
     *
     * @param p
     */
    public void addPlayer(Player p, PlayerRole pr) {
        if (team.getStatus().equals(TeamStatus.Open)) {
            boolean exists = false;
            for (PlayerRoleInTeam prit : p.getRoleInTeams()) {
                if (prit.getTeam() == team) {
                    EventLogger.getInstance().saveLog("This player is already part of the team");
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                PlayerRoleInTeam newPrit = new PlayerRoleInTeam(p, this.team, pr);
                team.addPlayerRoleInTeam(newPrit);
                p.addPlayerRoleInTeam(newPrit);
                EventLogger.getInstance().saveLog("The player has been added successfully");
            }
        } else {
            EventLogger.getInstance().saveLog("The team status is closed, you can't do any operations");
        }
    }

    /**
     * remove player from the team
     *
     * @param p
     */
    public void removePlayer(Player p) throws Exception {
        if (team.getStatus().equals(TeamStatus.Open)) {
            boolean exists = false;
            for (PlayerRoleInTeam prit : p.getRoleInTeams()) {
                if (prit.getTeam() == team) {
                    team.getPlayers().remove(prit);
                    p.getRoleInTeams().remove(prit);
                    if (p.getRoleInTeams().size() == 0) {
                        p.getMember().removePlayer(p);
                    }
                    EventLogger.getInstance().saveLog("The player has been removed successfully");
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                EventLogger.getInstance().saveLog("This player is not part of the team's player");
            }
        } else {
            EventLogger.getInstance().saveLog("The team status is closed, you can't do any operations");
        }
    }

    /**
     * add new field to the team
     *
     * @param f
     */
    public void setNewField(Field f) {
        if (team.getStatus().equals(TeamStatus.Open)) {
            team.setField(f);
            EventLogger.getInstance().saveLog("The field has been added successfully");
        } else {
            EventLogger.getInstance().saveLog("The team status is closed, you can't do any operations");
        }
    }

    /**
     * remove the object from all occurrences
     *
     * @return
     */
    @Override
    public boolean removeYourself() throws Exception {
        if (team.getStatus().equals(TeamStatus.Open)) {
            if (this.team.getTeam_owners().size() > 1) {
                team.getTeam_owners().remove(this);
                for (IRole r : appointments) {
                    if (r instanceof TeamOwner) {
                        removeTeamOwner((TeamOwner) r);
                    } else if (r instanceof TeamManager) {
                        removeTeamManager((TeamManager) r);
                    }
                }
                appointments.clear();
                this.team = null;
                this.appointer = null;
                this.member.removeTeamOwner(this);
                EventLogger.getInstance().saveLog("The team owner has been removed successfully");
                return true;
            }
        } else {
            EventLogger.getInstance().saveLog("The team status is closed, you can't do any operations");
        }
        return false;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public TeamOwner getAppointer() {
        return appointer;
    }

    public void setAppointer(TeamOwner appointer) {
        this.appointer = appointer;
    }

    @JsonIgnore
    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public ArrayList<IRole> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<IRole> appointments) {
        this.appointments = appointments;
    }

    @Override
    public void teamUpdate(TeamStatus ts) {
        EventLogger.getInstance().saveLog("The team status has been changed to " + ts.toString());
    }

    public void registerToTeamStatus() {
        this.team.register(this);
    }

    public void removeFromTeamStatus() {
        this.team.remove(this);
    }


}
