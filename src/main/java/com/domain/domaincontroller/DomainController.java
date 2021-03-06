package com.domain.domaincontroller;


import com.data.Dao;
import com.domain.logic.AssociationSystem;
import com.domain.logic.RecommenderSystem;
import com.domain.logic.data_types.GameIdentifier;
import com.domain.logic.enums.EventType;
import com.domain.logic.enums.PlayerRole;
import com.domain.logic.football.*;
import com.domain.logic.managers.ManageLeagues;
import com.domain.logic.managers.ManageMembers;
import com.domain.logic.managers.ManageSeasons;
import com.domain.logic.managers.ManageTeams;
import com.domain.logic.policies.GameSettingPolicy;
import com.domain.logic.policies.RankingPolicy;
import com.domain.logic.policies.game_setting_policies.IGameSettingPolicyStrategy;
import com.domain.logic.policies.game_setting_policies.OneMatchEachPairSettingPolicy;
import com.domain.logic.policies.game_setting_policies.TwoMatchEachPairSettingPolicy;
import com.domain.logic.roles.*;
import com.domain.logic.users.Guest;
import com.domain.logic.users.IGameObserver;
import com.domain.logic.users.Member;
import com.domain.logic.users.SystemManagerMember;
import com.logger.ErrorLogger;
import com.logger.EventLogger;
import com.service.SendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;


@Service
public class DomainController {

    @Autowired
    private final Dao dao;

    public DomainController(@Qualifier("DBCommunicator") Dao dao) {
        this.dao = dao;
    }

    // ------------------------1.Login---------------------------
    public Member login(String username, String password, String firstName, String lastName){
        try {
            Guest guest = new Guest(firstName, lastName);
            EventLogger.getInstance().saveLog("attempt to login with credentials: \""+username+"\" , \""+password+"\"");
            Member member = guest.login(username, password);

            EventLogger.getInstance().saveLog("member: \""+username+"'\" has logged in");
            return member;
        } catch (Exception e) {
            ErrorLogger.getInstance().saveError(e.getMessage());
            return null;
        }
    }


    // ------------------------2.Create Team---------------------
    public boolean createTeam(String teamOwnerUsername, String teamName, String fieldCountry, String fieldState, String fieldCity, String fieldPostalCode){
        try {
            Member member = AssociationSystem.getInstance().findConnectedUser(teamOwnerUsername);
            TeamOwner teamOwner = (TeamOwner) member.getSpecificRole(TeamOwner.class);
            EventLogger.getInstance().saveLog("\""+teamOwnerUsername+"\" attempting to create a new team with team name: \""+teamName+"\"");
            teamOwner.createTeam(teamName, new Field(fieldCountry, fieldState, fieldCity, fieldPostalCode));

            EventLogger.getInstance().saveLog("new team :\""+teamName+"\" created by member: \""+member.getUserName()+"\"");
            return true;
        } catch (Exception e) {
            ErrorLogger.getInstance().saveError(e.getMessage());
            return false;
        }
    }

    // ------------------------3.1.Define GameSetting Policy-------
    public boolean defineGameSettingPolicy(String associationAgentUsername, String leagueName, int seasonYear, String gameSettingPolicy){
        try{
            Member member = AssociationSystem.getInstance().findConnectedUser(associationAgentUsername);
            AssociationAgent associationAgent = (AssociationAgent) member.getSpecificRole(AssociationAgent.class);
            SeasonInLeague seasonInLeague = dao.findSeasonInLeague(seasonYear, leagueName);

            EventLogger.getInstance().saveLog("\""+associationAgentUsername+"\" attempting to change Game Setting Policy in League: \""+leagueName+"\" at Season: "+seasonYear+" to "+gameSettingPolicy+" match each pair");
            // creating the appropriate game setting policy
            if (gameSettingPolicy.equals("one")){
                associationAgent.setGameSettingPolicy(seasonInLeague.getLeague(), seasonInLeague.getSeason(), new GameSettingPolicy(seasonInLeague.getPolicy(), new OneMatchEachPairSettingPolicy()));
            }
            else if(gameSettingPolicy.equals("two")){
                associationAgent.setGameSettingPolicy(seasonInLeague.getLeague(), seasonInLeague.getSeason(), new GameSettingPolicy(seasonInLeague.getPolicy(), new TwoMatchEachPairSettingPolicy()));
            }

            EventLogger.getInstance().saveLog("\""+associationAgentUsername+"\n has changed the Game Setting Policy in League: \""+leagueName+"\" at Season: "+seasonYear+" to "+gameSettingPolicy+" match each pair");
            return true;
        } catch (Exception e) {
            ErrorLogger.getInstance().saveError(e.getMessage());
            return false;
        }
    }

    // ------------------------3.2.Define Game Ranking Policy------
    public boolean defineGameRankingPolicy(String associationAgentUsername, String leagueName, int seasonYear, int win, int goals, int draw, int yellowCards, int redCards){
        try{
            Member member = AssociationSystem.getInstance().findConnectedUser(associationAgentUsername);
            AssociationAgent associationAgent = (AssociationAgent) member.getSpecificRole(AssociationAgent.class);
            SeasonInLeague seasonInLeague = dao.findSeasonInLeague(seasonYear, leagueName);

            EventLogger.getInstance().saveLog("\""+associationAgentUsername+" attempting to change Game Ranking Policy in League: \""+leagueName+"\" at Season: "+seasonYear+" to:\nwin:"+win+"\ngoals:"+goals+"\ndraw:"+draw+"\nYellow Cards:"+yellowCards+"\nRed Cards:"+redCards);
            // creating the appropriate game ranking policy
            associationAgent.setRankingPolicy(seasonInLeague.getLeague(), seasonInLeague.getSeason(), new RankingPolicy(seasonInLeague.getPolicy(), win, goals, draw, yellowCards, redCards));

            EventLogger.getInstance().saveLog("\""+associationAgentUsername+"\" has changed the Game Ranking Policy in League: \""+leagueName+"\" at Season: "+seasonYear+" to:\nwin:"+win+"\ngoals:"+goals+"\ndraw:"+draw+"\nYellow Cards:"+yellowCards+"\nRed Cards:"+redCards);
            return true;
        } catch (Exception e) {
            ErrorLogger.getInstance().saveError(e.getMessage());
            return false;
        }
    }

    // ------------------------4.1.Referee Adds Events To Game------
    public Event addGameEvent(String refereeUsername, int gameID, int gameMinute, String description, String type, String playerUsername){
        try{
            login("main Referee asaf", "asafpass", "123", "!23");
            Member memberReferee = AssociationSystem.getInstance().findConnectedUser(refereeUsername);
            Referee referee = (Referee) memberReferee.getSpecificRole(Referee.class);
            EventType eventType = EventType.strToEventType(type);

            // retrieve game fans
            ArrayList<String[]> fansUsernames = dao.getGameFans(gameID);
            // divide to not-connected and connected users
            ArrayList<IGameObserver> connectedGameFans = new ArrayList<>();
            ArrayList<String> notConnectedFansEmail = new ArrayList<>();
            for (String[] gameFan: fansUsernames){
                try{
                    Member connectedMember = AssociationSystem.getInstance().findConnectedUser(gameFan[0]);
                    connectedGameFans.add(connectedMember);
                }
                catch (Exception e){
                    // not connected
                    String email = gameFan[1];
                    notConnectedFansEmail.add(email);
                }
            }

            // creating a game with connected fans
            String[] teamNames = dao.getTeamNamesOfGame(gameID);
            String hostTeamName = teamNames[0];
            String guestTeamName = teamNames[1];


            EventLogger.getInstance().saveLog("\""+refereeUsername+"\" attempting to add new game event in gameID: "+gameID);
            // create the game event
            Event event = referee.createGameEvent(gameMinute, description, eventType, gameID, hostTeamName, guestTeamName, playerUsername);

            // sending emails to all unconnected users
            for(String email : notConnectedFansEmail){
                SendMail.sendToUser("NEW GAME EVENT!!!", email, event.toString());
            }

            EventLogger.getInstance().saveLog("new game event was added to gameID: "+gameID+" by referee: \""+refereeUsername+"\"");
            return event;
        } catch (Exception e) {
            ErrorLogger.getInstance().saveError(e.getMessage());
            return null;
        }
    }

    // ------------------------4.2.Referee Create Game Report-------
    public boolean createGameReport(String refereeUsername, int gameID, String report){
        try{
            Member member = AssociationSystem.getInstance().findConnectedUser(refereeUsername);
            Referee referee = (Referee) member.getSpecificRole(Referee.class);
            EventLogger.getInstance().saveLog("\""+refereeUsername+"\" attempting to create game report in gameID: "+gameID);
            // create game report
            referee.createReport(gameID, report);

            EventLogger.getInstance().saveLog("\""+refereeUsername+"\" created game report in gameID: "+gameID);
            return true;
        } catch (Exception e) {
            ErrorLogger.getInstance().saveError(e.getMessage());
            return false;
        }
    }

    // ------------------------Additions-----------------------------
    public boolean logout(String username) {
        try {
            EventLogger.getInstance().saveLog("\""+username+"\n attempting to logout");
            Member member = AssociationSystem.getInstance().findConnectedUser(username);
            AssociationSystem.getInstance().logOutUser(member);
            EventLogger.getInstance().saveLog("\""+username+"\n has logged out");
            return true;
        } catch (Exception e) {
            ErrorLogger.getInstance().saveError("tyring to logout an un-connected user");
            return false;
        }
    }
    public void performResetSystem(String sysManagerUserName) {
        try {
            Member member = AssociationSystem.getInstance().findConnectedUser(sysManagerUserName);
            if(member instanceof SystemManagerMember){
                SystemManagerMember systemManagerMember = (SystemManagerMember) member;
                EventLogger.getInstance().saveLog("performing reset system...");
                systemManagerMember.resetSystem(dao);
                EventLogger.getInstance().saveLog("reset system successfully done");
            }
        } catch (Exception e) {
            ErrorLogger.getInstance().saveError(e.getMessage());
        }
    }
    public ArrayList<String> getAllLeaguesNames(){
        ArrayList<String> allLeagueNames = new ArrayList<>();
        EventLogger.getInstance().saveLog("attempting to find and return all league names");
        try {
            allLeagueNames = dao.getAllLeaguesNames();
            EventLogger.getInstance().saveLog("returning all league names");
        } catch (Exception e) {
            ErrorLogger.getInstance().saveError(e.getMessage());
        }
        return allLeagueNames;
    }
    public GameIdentifier getRefereeActiveGame(String refereeUsername){
        GameIdentifier gameIdentifier = null;
        EventLogger.getInstance().saveLog("attempting to find current active game for referee: \""+refereeUsername+"\"");
        try{
            gameIdentifier = dao.getRefereeActiveGame(refereeUsername);
            EventLogger.getInstance().saveLog("returning active game for referee: \""+refereeUsername+"\"");
        } catch (Exception e) {
            ErrorLogger.getInstance().saveError(e.getMessage());
        }
        return gameIdentifier;
    }
    public GameIdentifier getRefereeReportActiveGame(String refereeUsername){
        GameIdentifier gameIdentifier = null;
        EventLogger.getInstance().saveLog("attempting to find current active game for referee: \""+refereeUsername+"\"");
        try{
            gameIdentifier = dao.getRefereeReportActiveGame(refereeUsername);
            EventLogger.getInstance().saveLog("returning active game for referee: \""+refereeUsername+"\"");
        } catch (Exception e) {
            ErrorLogger.getInstance().saveError(e.getMessage());
        }
        return gameIdentifier;
    }
    public ArrayList<String> getAllTeamPlayers(String teamName){
        ArrayList<String> allTeamPlayers = new ArrayList<>();
        EventLogger.getInstance().saveLog("attempting to find and return all players of team: \""+teamName+"\"");
        try{
            allTeamPlayers = dao.getAllTeamPlayers(teamName);
            EventLogger.getInstance().saveLog("returning all players of team: \""+teamName+"\"");
        } catch (Exception e) {
            ErrorLogger.getInstance().saveError(e.getMessage());
        }
        return allTeamPlayers;
    }

    public SeasonInLeague findSeasonInLeague(int i, String leagueName) {
        SeasonInLeague seasonInLeague = null;
        EventLogger.getInstance().saveLog("attempting to find and return season in league for league: \""+leagueName+"\"");
        try {
            seasonInLeague = dao.findSeasonInLeague(i, leagueName);
            EventLogger.getInstance().saveLog("returning season in league to league: \""+leagueName+"\"");
        } catch (Exception e) {
            ErrorLogger.getInstance().saveError(e.getMessage());
        }
        return seasonInLeague;
    }

    public ArrayList<Integer> getAllLeagueSeasons(String leagueName){
        ArrayList<Integer> allLeagueSeasons = new ArrayList<>();
        EventLogger.getInstance().saveLog("attempting to find all league seasons for league: \""+leagueName+"\"");
        try {
            allLeagueSeasons = dao.getAllLeagueSeasons(leagueName);
            EventLogger.getInstance().saveLog("returning all seasons for league: \""+leagueName+"\"");
        } catch (Exception e) {
            ErrorLogger.getInstance().saveError(e.getMessage());
        }
        return allLeagueSeasons;
    }

    /**
     * @return all team owners of a specific team
     * @param teamName
     */
    public ArrayList<TeamOwner> findAllTeamOwner(String teamName) {
        //TODO
        return null;
    }

    public ArrayList<TeamManager> findAllTeamManager(String teamName) {
        //TODO
        return null;
    }

    /**
     * just a test - not as stav wants
     */
    public void addLeagueByObject(League league){
        dao.addLeague(league);
    }

    /**
     * just a test - as stav wants
     */
    public void addLeagueByName(String leagueName){
        dao.addLeague(new League(leagueName));
    }


    // System Manager Member UC's

    /**
     * "Close Team Permanently" UC
     */
    public void performCloseTeamPermanently(SystemManagerMember systemManagerMember, Team team) throws Exception {
        systemManagerMember.closeTeamPermanently(team);
    }

    /**
     * "Remove Member" func
     * @param sysManager
     * @param toDelete
     * @return
     */
    public boolean performRemoveMember(SystemManagerMember sysManager, Member toDelete) throws Exception {
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
        try {
            guest.login(userName,password);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    public void performAppointCoach(TeamOwner teamOwner, Member member) throws Exception {
        Coach coach = new Coach(member);
        teamOwner.appointCoach(coach);
    }

    /**
     * "Remove Asset From Team" UC
     */
    public void performRemoveCoach(TeamOwner teamOwner, Coach coach) throws Exception {
        teamOwner.removeCoach(coach);
    }

    /**
     * "Add Asset to Team" UC
     */
    public void performAddPlayer(TeamOwner teamOwner, Member member, PlayerRole playerRole) throws Exception {
        Player player = new Player(member, new Date());
        teamOwner.addPlayer(player, playerRole);
    }

    /**
     * "Remove Asset from Team" UC
     */
    public void performRemovePlayer(TeamOwner teamOwner, Player player) throws Exception {
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
    public void performAppointTeamOwner(TeamOwner teamOwner, Member member) throws Exception {
        teamOwner.appointTeamOwner(member);
    }

    /**
     * "Remove Team Owner" UC
     */
    public void performRemoveTeamOwner(TeamOwner teamOwner, TeamOwner otherTeamOwner) throws Exception {
        teamOwner.removeTeamOwner(otherTeamOwner);
    }

    /**
     * "Appoint Team Manager" UC
     */
    public void performAppointTeamManager(TeamOwner teamOwner, Member member) throws Exception {
        teamOwner.appointTeamManager(member);
    }

    /**
     * "Remove Team Manager" UC
     */
    public void performRemoveTeamManager(TeamOwner teamOwner, TeamManager teamManager) throws Exception {
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
        try {
            associationAgent.createLeague(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * "Create Season in League" UC
     */
    public void performCreateSeasonInLeague(AssociationAgent associationAgent, League league, int year){
        try {
            associationAgent.createSeasonInLeague(league, year);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * "Appoint Referee" UC
     */
    public void performAppointReferee(AssociationAgent associationAgent, Member member) throws Exception {
        associationAgent.appointReferee(member);
    }

    /**
     * "Remove Referee" UC
     */
    public void performRemoveReferee(AssociationAgent associationAgent, Referee referee) throws Exception {
        associationAgent.removeReferee(referee);
    }

    /**
     * "Set Referee in League in Season" UC
     */
    public void performSetRefereeInLeagueInSeason(AssociationAgent associationAgent, Referee referee, League league, Season season){
        try {
            associationAgent.setRefereeInLeague(referee, league, season);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
            associationAgent.setGameSettingPolicy(league, season, gameSettingPolicy);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public ArrayList<SystemManagerMember> findAllSystemManagers() {
        //TODO call DAO
        return null;
    }
}
