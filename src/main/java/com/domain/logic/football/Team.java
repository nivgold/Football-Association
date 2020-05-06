package com.domain.logic.football;


import com.domain.domaincontroller.managers.ManageTeams;
import com.domain.logic.data_types.PersonalPage;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.roles.Coach;
import com.domain.logic.roles.ITeamObserver;
import com.domain.logic.roles.TeamManager;
import com.domain.logic.roles.TeamOwner;

import java.util.ArrayList;

public class Team implements IPersonalPageable, ITeamObservable {
    private ArrayList<Game> gameHost;
    private ArrayList<Game> gameGuest;
    private ArrayList<Season> seasons;
    private ArrayList<PlayerRoleInTeam> players;
    private ArrayList<TeamManager> Team_managers;
    private ArrayList<TeamOwner> Team_owners;
    private Field field;
    private ArrayList<ITeamObserver> teamObservers;

    private ArrayList<CoachInTeam> coachesInTeam;
    private PersonalPage personal_page;
    private String teamName;
    private TeamStatus status;

    public Team(String teamName, TeamStatus status, TeamOwner team_owner, Field field) {
        this.teamName = teamName;
        this.status = status;
        this.field = field;
        field.setOwnerTeam(this);

        this.seasons = new ArrayList<>();
        this.gameHost = new ArrayList<>();
        this.Team_owners = new ArrayList<>();
        this.Team_owners.add(team_owner);
        team_owner.setTeam(this);
        this.teamObservers = new ArrayList<>();
        this.players = new ArrayList<>();
        this.Team_managers = new ArrayList<>();
        this.gameGuest = new ArrayList<>();
        this.coachesInTeam = new ArrayList<>();
        this.personal_page = new PersonalPage(this);
        ManageTeams.getInstance().addTeam(this);
    }

    public Team(ArrayList<Game> gameHost, ArrayList<Season> season, ArrayList<PlayerRoleInTeam> players, ArrayList<TeamManager> team_managers, ArrayList<TeamOwner> team_owners, ArrayList<Game> gameGuest, Field field, ArrayList<CoachInTeam> coaches, String teamName, TeamStatus status) {
        this.gameHost = gameHost;
        this.seasons = season;
        this.players = players;
        Team_managers = team_managers;
        Team_owners = team_owners;
        this.gameGuest = gameGuest;
        this.field = field;
        this.coachesInTeam = coaches;
        this.teamName = teamName;
        this.status = status;
        this.personal_page = new PersonalPage(this);
        this.teamObservers = new ArrayList<>();
        ManageTeams.getInstance().addTeam(this);
        field.setOwnerTeam(this);
    }


    @Override
    public void updatePersonalPage() {
        this.personal_page.updatePage(this.toString());
    }

    public boolean removeTeamPermanently() {
        this.setStatus(TeamStatus.PermanentlyClosed);
        //notify observers
        for (ITeamObserver teamObserver : teamObservers) {
            teamObserver.teamUpdate(TeamStatus.PermanentlyClosed);
        }
        for(PlayerRoleInTeam playerRoleInTeam : this.players) { //delete playerRoleInTeam from player
            playerRoleInTeam.getPlayer().getRoleInTeams().remove(playerRoleInTeam);
        }
        for (CoachInTeam coachInTeam : coachesInTeam ) { //delete coachInTeam from coach
            Coach coach = coachInTeam.getCoach();
            coach.getTeams().remove(coachInTeam);
        }
        for (TeamOwner teamOwner : Team_owners) { //delete teamOwner role from member
            teamOwner.getMember().getRoles().remove(teamOwner);
        }
        for (TeamManager teamManager : Team_managers) { //delete teamManager role from member
            teamManager.getMember().getRoles().remove(teamManager);
        }
        this.Team_owners.removeAll(this.Team_owners);
        this.Team_managers.removeAll(this.Team_managers);
        this.coachesInTeam.removeAll(this.coachesInTeam);
        this.players.removeAll(this.getPlayers());
        return true;
    }

    @Override
    public String toString() {
        ArrayList<Game> allTeamGames = new ArrayList<>();
        for (int i = 0; i < this.gameGuest.size(); i++) {
            allTeamGames.add(this.gameGuest.get(i));
        }
        for (int i = 0; i < this.gameHost.size(); i++) {
            allTeamGames.add(this.gameHost.get(i));
        }

        return "Business.football.Team{" +
                "season=" + seasons +
                " players=" + players +
                " Team_managers=" + Team_managers +
                " Team_owners=" + Team_owners +
                " field=" + field +
                " coach=" + coachesInTeam +
                " teamName='" + teamName + '\'' +
                " status=" + status +
                " games=" + allTeamGames +
                '}';
    }

    public ArrayList<CoachInTeam> getCoaches() {
        return coachesInTeam;
    }

    public void addCoachInTeam(CoachInTeam coachInTeam){
        this.coachesInTeam.add(coachInTeam);
    }

    public void addPlayerRoleInTeam(PlayerRoleInTeam playerRoleInTeam){
        this.players.add(playerRoleInTeam);
    }

    public ArrayList<PlayerRoleInTeam> getPlayers() {
        return players;
    }

    public String getTeamName() {
        return teamName;
    }

    @Override
    public void register(ITeamObserver teamObserver) {
        this.teamObservers.add(teamObserver);
    }

    @Override
    public void remove(ITeamObserver teamObserver) {
        this.teamObservers.remove(teamObserver);
    }

    @Override
    public void notifyObservers() {
        for (ITeamObserver teamObserver: this.teamObservers)
            teamObserver.teamUpdate(this.status);
    }

    public ArrayList<Game> getGameHost() {
        return gameHost;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public ArrayList<TeamManager> getTeam_managers() {
        return Team_managers;
    }

    public ArrayList<TeamOwner> getTeam_owners() {
        return Team_owners;
    }

    public ArrayList<Game> getGameGuest() {
        return gameGuest;
    }

    public Field getField() {
        return field;
    }

    public PersonalPage getPersonal_page() {
        return personal_page;
    }

    public TeamStatus getStatus() {
        return status;
    }

    public void setStatus(TeamStatus status) {
        this.status = status;
        notifyObservers();
    }

    public void setGameHost(ArrayList<Game> gameHost) {
        this.gameHost = gameHost;
    }

    public void setSeasons(ArrayList<Season> seasons) {
        this.seasons = seasons;
    }

    public void setPlayers(ArrayList<PlayerRoleInTeam> players) {
        this.players = players;
    }

    public void setTeam_managers(ArrayList<TeamManager> team_managers) {
        Team_managers = team_managers;
    }

    public void setTeam_owners(ArrayList<TeamOwner> team_owners) {
        Team_owners = team_owners;
    }

    public void setGameGuest(ArrayList<Game> gameGuest) {
        this.gameGuest = gameGuest;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void setTeamObservers(ArrayList<ITeamObserver> teamObservers) {
        this.teamObservers = teamObservers;
    }

    public void setCoaches(ArrayList<CoachInTeam> coaches) {
        this.coachesInTeam = coaches;
    }

    public void setPersonal_page(PersonalPage personal_page) {
        this.personal_page = personal_page;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public ArrayList<ITeamObserver> getTeamObservers() {
        return teamObservers;
    }

    public ArrayList<CoachInTeam> getCoachesInTeam() {
        return coachesInTeam;
    }

    public void setCoachesInTeam(ArrayList<CoachInTeam> coachesInTeam) {
        this.coachesInTeam = coachesInTeam;
    }
}