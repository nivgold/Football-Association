package com.domain.logic.roles;

import com.domain.logic.data_types.PersonalPage;
import com.domain.logic.football.CoachInTeam;
import com.domain.logic.football.IPersonalPageable;
import com.domain.logic.users.Member;
import com.logger.Logger;

import java.util.ArrayList;

public class Coach implements IPersonalPageable, IRole {

    private String qualification;
    private PersonalPage personalPage;
    private ArrayList<CoachInTeam> teams;
    private Member member;

    /**
     * Business.roles.Coach constructor
     * @param member
     */
    public Coach(Member member) {
        this.member = member;
        this.teams = new ArrayList<>();
        this.qualification = "New";
        this.personalPage = new PersonalPage(this);
        member.getRoles().add(this);
    }

    /**
     *
     * @param qualification
     * @param teams
     * @param member
     */
    public Coach(String qualification, ArrayList<CoachInTeam> teams, Member member) {
        this.qualification = qualification;
        this.teams = teams;
        this.personalPage = new PersonalPage(this);
        this.member = member;
        member.getRoles().add(this);
    }

    public void addCoachInTeam(CoachInTeam coachInTeam){
        this.teams.add(coachInTeam);
    }

    /**
     * notify the personal page about changes
     */
    @Override
    public void updatePersonalPage() {
        this.personalPage.updatePage(this.toString());
    }

    @Override
    public String toString() {
        return "Business.roles.Coach{" +
                "qualification='" + qualification + '\'' +
                " teams=" + teams +
                '}';
    }

    /**
     * remove the object from all occurrences
     * @return
     */
    @Override
    public boolean removeYourself() {
        try {
            for (CoachInTeam t : teams) {
                t.getTeam().getCoaches().remove(t);
            }
            teams.clear();
            member.getRoles().remove(this);
            Logger.getInstance().saveLog("The coach has been removed successfully");
            return true;
        }
        catch (Exception e) {
            e.getStackTrace();
        }
        Logger.getInstance().saveLog("An error occurred - couldn't remove the coach");
        return  false;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
        updatePersonalPage();
    }

    public PersonalPage getPersonalPage() {
        return personalPage;
    }

    public void setPersonalPage(PersonalPage personalPage) {
        this.personalPage = personalPage;
        updatePersonalPage();
    }

    public ArrayList<CoachInTeam> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<CoachInTeam> teams) {
        this.teams = teams;
        updatePersonalPage();
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
        updatePersonalPage();
    }

}
