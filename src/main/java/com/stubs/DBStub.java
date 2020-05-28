package com.stubs;

import com.data.Dao;
import com.domain.logic.data_types.GameIdentifier;
import com.domain.logic.enums.EventType;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.*;
import com.domain.logic.policies.GameSettingPolicy;
import com.domain.logic.policies.Policy;
import com.domain.logic.policies.RankingPolicy;
import com.domain.logic.policies.game_setting_policies.OneMatchEachPairSettingPolicy;
import com.domain.logic.policies.game_setting_policies.TwoMatchEachPairSettingPolicy;
import com.domain.logic.roles.*;
import com.domain.logic.users.Member;
import com.domain.logic.users.SystemManagerMember;

import java.util.ArrayList;

public class DBStub implements Dao {

    private static DBStub dbStub;

    public static ArrayList<Season> seasons = new ArrayList<>();
    public static ArrayList<League> leagues = new ArrayList<>();
    public static ArrayList<SeasonInLeague> seasonInLeagues = new ArrayList<>();
    public static ArrayList<Policy> policies = new ArrayList<>();
    public static ArrayList<GameSettingPolicy> gameSettingPolicies = new ArrayList<>();
    public static ArrayList<Member> members = new ArrayList<>();
    public static ArrayList<Team> teams = new ArrayList<>();
    public static ArrayList<Referee> referees = new ArrayList<>();
    public static ArrayList<Player> players=  new ArrayList<>();

    private DBStub(){
    }

    public static DBStub getInstance(){
        if (dbStub == null)
            dbStub = new DBStub();

        return dbStub;
    }

    @Override
    public boolean checkIfTeamExists(String teamName) {
        return false;
    }

    @Override
    public Game findGame(int gameID) {
        return null;
    }

    @Override
    public Member findMember(String username) throws Exception {
        for (Member member : this.members){
            if (member.getUserName().equals(username))
                return member;
        }
        return null;
    }

    @Override
    public boolean hasTeam(String teamOwnerUsername) {
        for (Member member : this.members){
            if (member.getUserName().equals(teamOwnerUsername)){
                try {
                    TeamOwner teamOwner = (TeamOwner) member.getSpecificRole(TeamOwner.class);
                    if (teamOwner.getTeam()==null)
                        return false;
                    return true;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public boolean addSeasonInLeague(int year, String leagueName) throws Exception {
       seasonInLeagues.add(new SeasonInLeague(this.findLeague(leagueName), this.findSeason(year)));
       return true;
    }

    @Override
    public void appointReferee(String userName) throws Exception {
        Member member = findMember(userName);
        Referee referee = new Referee(member);
    }

    @Override
    public void removeReferee(String userName) throws Exception {

    }

    @Override
    public SeasonInLeague findSeasonInLeague(int seasonYear, String leagueName) throws Exception {
        for (SeasonInLeague seasonInLeague : this.seasonInLeagues){
            if (seasonInLeague.getSeason().getYear()==seasonYear && seasonInLeague.getLeague().getLeagueName().equals(leagueName))
                return seasonInLeague;
        }
        return null;
    }

    @Override
    public ArrayList<String[]> getGameFans(int gameID) throws Exception {
        return null;
    }

    @Override
    public void setGameReport(int gameID, String report) throws Exception {

    }

    @Override
    public void addGameEvent(int gameID, int gameMinute, String description, EventType type, String playerUsername, int changeScore) throws Exception {

    }

    @Override
    public boolean isRefereeAuthorized(String refereeUsername, int gameID) throws Exception {
        return false;
    }

    @Override
    public boolean isReportAuthorized(String refereeUsername, int gameID) throws Exception {
        return false;
    }

    @Override
    public ArrayList<String> getAllTeamNames() throws Exception {
        return null;
    }

    @Override
    public SeasonInLeague findSeasonInLeague(int seasonYear, String leagueName) throws Exception {
        for (SeasonInLeague seasonInLeague : this.seasonInLeagues){
            if (seasonInLeague.getSeason().getYear()==seasonYear && seasonInLeague.getLeague().getLeagueName().equals(leagueName))
                return seasonInLeague;
        }
        return null;
    }

    @Override
    public void setGameSettingPolicy(int seasonYear, String leagueName, boolean gameSettingPolicyField) throws Exception {
        SeasonInLeague seasonInLeague = findSeasonInLeague(seasonYear, leagueName);
        Policy policy = seasonInLeague.getPolicy();
        if (gameSettingPolicyField){
            policy.setGameSettingPolicy(new OneMatchEachPairSettingPolicy());
        }
        else{
            policy.getGameSettingPolicy().setSettingStrategy(new TwoMatchEachPairSettingPolicy());
        }
    }

    @Override
    public void setGameRankingPolicy(int seasonYear, String leagueName, int win, int goals, int draw, int yellowCards, int redCards) throws Exception {
        SeasonInLeague seasonInLeague = findSeasonInLeague(seasonYear, leagueName);
        Policy policy = seasonInLeague.getPolicy();
        RankingPolicy rankingPolicy = new RankingPolicy(policy, win, goals, draw, yellowCards, redCards);
        policy.setRankingPolicy(rankingPolicy);
    }

    @Override
    public GameIdentifier getRefereeActiveGame(String refereeUsername) throws Exception {
        return null;
    }

    @Override
    public ArrayList<String> getAllTeamPlayers(String teamName) throws Exception {
        return null;
    }

    @Override
    public League findLeague(String leagueName) throws Exception {
        for (League league : this.leagues){
            if (league.getLeagueName().equals(leagueName))
                return league;
        }
        return null;
    }

    @Override
    public boolean removeLeague(String leagueName) {
        return false;
    }

    @Override
    public boolean addLeague(League league) {
        leagues.add(league);
        return true;
    }

    @Override
    public ArrayList<League> findLeagueByName(String keywords) {
        return null;
    }

    @Override
    public ArrayList<String> getAllLeaguesNames() throws Exception {
        ArrayList<String> leaguesNames = new ArrayList<>();
        for(League league : leagues){
            leaguesNames.add(league.getLeagueName());
        }
        return leaguesNames;
    }

    @Override
    public void removeAllLeagues() {

    }

    @Override
    public Member findMember(String userName, String hashPassword) throws Exception {
        for (Member member : this.members){
            if (member.getUserName().equals(userName) && member.getPasswordHash().equals(hashPassword))
                return member;
        }
        return null;
    }

    @Override
    public Member findMember(Member member) {
        return null;
    }

    @Override
    public boolean removeMember(String userName, String passwordHash) {
        return false;
    }

    @Override
    public ArrayList<Member> findMemberByName(String name) {
        return null;
    }

    @Override
    public ArrayList<Member> findMemberByUsername(String userName) {
        return null;
    }

    @Override
    public boolean addMember(Member member) {
        members.add(member);
        return true;
    }

    @Override
    public ArrayList<Coach> findAllCoaches() {
        return null;
    }

    @Override
    public ArrayList<Player> findAllPlayers() {
        return null;
    }

    @Override
    public ArrayList<Referee> findAllReferees() {
        ArrayList<Referee> allRefs = new ArrayList<>();
        for(Member member: members){
            try {
                if(member.getSpecificRole(Referee.class) != null){
                    allRefs.add((Referee) member.getSpecificRole(Referee.class));
                }
            } catch (ClassNotFoundException e) {
                continue;
            }
        }
        return allRefs;
    }

    @Override
    public ArrayList<TeamOwner> findAllTeamOwner() {
        ArrayList<TeamOwner> allRefs = new ArrayList<>();
        for(Member member: members){
            try {
                if(member.getSpecificRole(TeamOwner.class) != null){
                    allRefs.add((TeamOwner) member.getSpecificRole(TeamOwner.class));
                }
            } catch (ClassNotFoundException e) {
                continue;
            }
        }
        return allRefs;
    }

    @Override
    public ArrayList<AssociationAgent> findAllAssociationAgent() {
        ArrayList<AssociationAgent> allRefs = new ArrayList<>();
        for(Member member: members){
            try {
                if(member.getSpecificRole(AssociationAgent.class) != null){
                    allRefs.add((AssociationAgent) member.getSpecificRole(AssociationAgent.class));
                }
            } catch (ClassNotFoundException e) {
                continue;
            }
        }
        return allRefs;
    }

    @Override
    public ArrayList<SystemManagerMember> findAllSystemManagers() {
        return null;
    }

    @Override
    public void removeAllMembers() {

    }

    @Override
    public Season findSeason(Integer seasonYear) throws Exception {
        for (Season season : this.seasons){
            if (season.getYear() == seasonYear)
                return season;
        }
        return null;
    }

    @Override
    public boolean removeSeason(Integer seasonYear) {
        return false;
    }

    @Override
    public boolean addSeason(Season season) {
        seasons.add(season);
        return true;
    }

    @Override
    public ArrayList<Season> findSeasonByYear(String keywords) {
        return null;
    }

    @Override
    public void removeAllSeasons() {

    }

    @Override
    public Team findTeam(String TeamName) {
        return null;
    }

    @Override
    public boolean removeTeam(String TeamName) {
        return false;
    }

    @Override
    public boolean addTeam(Team team) {
        teams.add(team);
        return true;
    }

    @Override
    public ArrayList<Team> findTeamByName(String keywords) {
        return null;
    }

    @Override
    public ArrayList<Team> getAllTeams() {
        return teams;
    }

    @Override
    public void removeAllTeams() {

    }

    @Override
    public void addTeam(String teamName, String country, String state, String city, String postalCode, TeamOwner teamOwner) throws Exception {
        for (Team team : this.teams){
            if (team.getTeamName().equals(teamName))
                return;
        }
        this.teams.add(new Team(teamName, TeamStatus.Open, teamOwner, new Field(country, state, city, postalCode)));
    }

    @Override
    public void resetSystem() {
        seasons = new ArrayList<>();
        leagues = new ArrayList<>();
        seasonInLeagues = new ArrayList<>();
        policies = new ArrayList<>();
        gameSettingPolicies = new ArrayList<>();
        members = new ArrayList<>();
        teams = new ArrayList<>();
        referees = new ArrayList<>();
        players=  new ArrayList<>();
    }
}
