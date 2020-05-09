package com.domain.domaincontroller;


import com.domain.logic.AssociationSystem;
import com.domain.logic.RecommenderSystem;
import com.domain.logic.enums.EventType;
import com.domain.logic.enums.PlayerRole;
import com.domain.logic.football.*;
import com.domain.logic.policies.GameSettingPolicy;
import com.domain.logic.policies.RankingPolicy;
import com.domain.logic.policies.game_setting_policies.IGameSettingPolicyStrategy;
import com.domain.logic.roles.*;
import com.domain.logic.users.Guest;
import com.domain.logic.users.Member;
import com.domain.logic.users.SystemManagerMember;

import java.util.ArrayList;
import java.util.Date;

public class DomainController {

    // System UC's

    /**
     * "Reset System" UC
     */
    public void performResetSystem(){
        AssociationSystem.getInstance().resetSystem();
    }

    // System Manager Member UC's

    /**
     * "Close Team Permanently" UC
     */
    public void performCloseTeamPermanently(SystemManagerMember systemManagerMember, Team team){
        systemManagerMember.closeTeamPermanently(team);
    }

    /**
     * "Remove Member" func
     * @param sysManager
     * @param toDelete
     * @return
     */
    public boolean performRemoveMember(SystemManagerMember sysManager, Member toDelete){
        return sysManager.deleteMember(toDelete);
    }

    /**
     * "Watch Complaints" UC
     */
    public void performReadComplaints(){

    }

    /**
     * "Watch System Management" UC (watch log file)
     */
    public void performWatchLogs(){

    }

    /**
     * "Build Recommender System Model" UC
     */
    public void performBuildRecommenderSystem(){

    }

    // Member UC's

    /**
     * "Register to Personal Page" UC
     */
    public void performFollowPersonalPage(){

    }

    /**
     * "Unfollow Personal Page" UC
     */
    public void performUnfollowPersonalPage(){

    }

    /**
     * "Register to Game" UC
     */
    public void performRegisterToGame(){

    }

    /**
     * "Unfollow Game" UC
     */
    public void performUnfollowGame(){

    }

    /**
     * "Write Complaint" UC
     */
    public void performWriteComplaint(){

    }

    /**
     * "Update Personal Details" UC
     */
    public void performUpdatePersonalDetails(){

    }

    // Guest UC's

    /**
     * "Register as Member" UC
     */
    public Member performRegisterAsMember(Guest guest, String userName, String password, String email, String country, String state, String city, String postalCode){
        return guest.registerAsMember(userName,password,email,country,state,city,postalCode);
    }

    /**
     * "Login" UC
     */
    public void performLogin(Guest guest, String userName, String password){
        guest.login(userName,password);

    }

    /**
     * "Search" UC
     * @param guest
     */
    public void performSearch(Guest guest, int key, String query){
        guest.search(key,query);

    }

    // Team Owner UC's

    /**
     * "Add Asset to Team" UC
     */
    public void performAppointCoach(TeamOwner teamOwner, Member member){
        Coach coach = new Coach(member);
        teamOwner.appointCoach(coach);
    }

    /**
     * "Remove Asset From Team" UC
     */
    public void performRemoveCoach(TeamOwner teamOwner, Coach coach){
        teamOwner.removeCoach(coach);
    }

    /**
     * "Add Asset to Team" UC
     */
    public void performAddPlayer(TeamOwner teamOwner, Member member, PlayerRole playerRole){
        Player player = new Player(member, new Date());
        teamOwner.addPlayer(player, playerRole);
    }

    /**
     * "Remove Asset from Team" UC
     */
    public void performRemovePlayer(TeamOwner teamOwner, Player player){
        teamOwner.removePlayer(player);
    }

    /**
     * "Update Asset of Team" UC
     */
    public void performSetNewField(TeamOwner teamOwner, Field field){
        teamOwner.setNewField(field);
    }

    /**
     * "Appoint Team Owner" UC
     */
    public void performAppointTeamOwner(TeamOwner teamOwner, Member member){
        teamOwner.appointTeamOwner(member);
    }

    /**
     * "Remove Team Owner" UC
     */
    public void performRemoveTeamOwner(TeamOwner teamOwner, TeamOwner otherTeamOwner){
        teamOwner.removeTeamOwner(otherTeamOwner);
    }

    /**
     * "Appoint Team Manager" UC
     */
    public void performAppointTeamManager(TeamOwner teamOwner, Member member){
        teamOwner.appointTeamManager(member);
    }

    /**
     * "Remove Team Manager" UC
     */
    public void performRemoveTeamManager(TeamOwner teamOwner, TeamManager teamManager){
        teamOwner.removeTeamManager(teamManager);
    }

    /**
     * "Close Team" UC
     */
    public void performCloseTeam(TeamOwner teamOwner){
        teamOwner.closeTeam();
    }

    /**
     * "Open Team" UC
     */
    public void performOpenTeam(TeamOwner teamOwner){
        teamOwner.openTeam();
    }

    // Association Agent UC's

    /**
     * "Create League" UC
     */
    public void performCreateLeague(AssociationAgent associationAgent, String name){
        associationAgent.createLeague(name);
    }

    /**
     * "Create Season in League" UC
     */
    public void performCreateSeasonInLeague(AssociationAgent associationAgent, League league, int year){
        associationAgent.createSeasonInLeague(league, year);
    }

    /**
     * "Appoint Referee" UC
     */
    public void performAppointReferee(AssociationAgent associationAgent, Member member){
        associationAgent.appointReferee(member);
    }

    /**
     * "Remove Referee" UC
     */
    public void performRemoveReferee(AssociationAgent associationAgent, Referee referee){
        associationAgent.removeReferee(referee);
    }

    /**
     * "Set Referee in League in Season" UC
     */
    public void performSetRefereeInLeagueInSeason(AssociationAgent associationAgent, Referee referee, League league, Season season){
        associationAgent.setRefereeInLeague(referee, league, season);
    }

    /**
     * "Set Ranking Policy" UC
     */
    public void performSetRankingPolicy(AssociationAgent associationAgent, League league, Season season, RankingPolicy rankingPolicy){
        try {
            associationAgent.setRankingPolicy(league, season, rankingPolicy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * "Set Game Setting Policy" UC
     */
    public void performSetGameSettingPolicy(AssociationAgent associationAgent, League league, Season season, IGameSettingPolicyStrategy policyStrategy){
        GameSettingPolicy gameSettingPolicy = new GameSettingPolicy(league.getSeasonLeaguePolicy().get(season), policyStrategy);
        associationAgent.setGameSettingPolicy(league, season, gameSettingPolicy);
    }

    // Referee

    /**
     * "Create Game Event" UC
     */
    public boolean performCreateGameEvent(Referee referee, int gameMinute, String description, EventType eventType, Date date, Game game, Player player){
        return referee.createGameEvent(gameMinute, description, eventType, date, game, player);
    }

    /**
     * "Edit Game Event" UC
     */
    public void performEditGameEvent(Referee referee, Game game, Event oldEvent, Event newEvent){
        referee.editGameEvent(game, oldEvent, newEvent);
    }

    // Additional Tests (NOT UC's)

    /**
     * "Recommender System" Requirement
     */
    public double performUseRecommenderSystem(Game game){
        return RecommenderSystem.getInstance().getChances(game);
    }

    public ArrayList<Game> getGamesToView(Referee referee) {
        ArrayList<Game> output = referee.getMain();
        output.addAll(referee.getSide());
        return output;
    }

    public String viewGame(Game chosenGame) {
        return chosenGame.toString();
    }

    public Member findMember(Member member){
        //TODO call DAO to find Member
        return ManageMembers.getInstance().findMember(member);
    }

    public Member findMember(String userName, String hashPassword){
        //TODO call DAO to find Member
        return ManageMembers.getInstance().findMember(userName, hashPassword);
    }

    public League findLeague(String leagueName){
        //TODO call DAO to find league
         return ManageLeagues.getInstance().findLeague(leagueName);
    }

    public Season findSeason(int seasonYear){
        //TODO call DAO to find season
         return ManageSeasons.getInstance().findSeason(seasonYear);
    }

    public Team findTeam(String teamName){
        //TODO call DAO to find Team
        return ManageTeams.getInstance().findTeam(teamName);
    }

    public ArrayList<Team> getALLTeams(){
        //TODO call DAO to get all Teams
        return ManageTeams.getInstance().getAllTeams();
    }
}
