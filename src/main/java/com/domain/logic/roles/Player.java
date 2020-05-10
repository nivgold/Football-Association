package com.domain.logic.roles;

import com.domain.logic.data_types.PersonalPage;
import com.domain.logic.football.Event;
import com.domain.logic.football.IPersonalPageable;
import com.domain.logic.football.PlayerRoleInTeam;
import com.domain.logic.users.Member;
import com.logger.Logger;

import java.util.ArrayList;
import java.util.Date;

public class Player implements IRole, IPersonalPageable {

    private static int countIDs = 0;
    private int playerID;
    private ArrayList<PlayerRoleInTeam> roleInTeams;
    private PersonalPage personalPage;
    private ArrayList<Event> events;
    private Member member;
    private Date birthDate;

    /**
     * player constructor
     */
    public Player(Member member, Date birthDate) {
        this.playerID = countIDs;
        countIDs++;
        this.roleInTeams = new ArrayList<>();
        this.personalPage = new PersonalPage(this);
        this.events = new ArrayList<>();
        this.member = member;
        this.birthDate = birthDate;
        member.addPlayer(this);
    }

    /**
     * remove the object from all occurrences
     * @return
     */
    @Override
    public boolean removeYourself() {
        try {
            for (PlayerRoleInTeam prit : roleInTeams) {
                prit.getTeam().getPlayers().remove(prit);
            }
            member.removePlayer(this);
            events.clear();
            roleInTeams.clear();
            Logger.getInstance().saveLog("The player has been removed successfully");
            return true;
        }
        catch (Exception e) {
            e.getStackTrace();
        }
        Logger.getInstance().saveLog("An error occurred - couldn't remove the player");
        return false;
    }

    /**
     * notify the personal page about changes
     */
    @Override
    public void updatePersonalPage() {
        personalPage.updatePage(this.toString());
    }

    @Override
    public String toString() {
        return "Business.roles.Player{" +
                "playerID=" + playerID +
                " roles=" + roleInTeams +
                " events=" + events +
                '}';
    }

    public int getPlayerID() {
        return playerID;
    }

    public PersonalPage getPersonalPage() {
        return personalPage;
    }

    public void setPersonalPage(PersonalPage personalPage) {
        this.personalPage = personalPage;
        updatePersonalPage();
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
        updatePersonalPage();
    }

    public Member getMember() {
        return member;
    }

    public void addPlayerRoleInTeam(PlayerRoleInTeam prit) {
        this.roleInTeams.add(prit);
    }

    public ArrayList<PlayerRoleInTeam> getRoleInTeams() {
        return roleInTeams;
    }

    public void setRoleInTeams(ArrayList<PlayerRoleInTeam> roleInTeams) {
        this.roleInTeams = roleInTeams;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

}
