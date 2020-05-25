package com.stubs;

import com.data.Dao;
import com.domain.logic.data_types.GameIdentifier;
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

import java.sql.SQLException;
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
    public static ArrayList<String> logs = new ArrayList<>();

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
    public void addLog(String data) throws SQLException {
        this.logs.add(data);
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
            policy.getGameSettingPolicy().setSettingStrategy(new OneMatchEachPairSettingPolicy());
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
        return false;
    }

    @Override
    public ArrayList<League> findLeagueByName(String keywords) {
        return null;
    }

    @Override
    public ArrayList<String> getAllLeaguesNames() throws Exception {
        return null;
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
        return false;
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
        return null;
    }

    @Override
    public ArrayList<TeamOwner> findAllTeamOwner() {
        return null;
    }

    @Override
    public ArrayList<AssociationAgent> findAllAssociationAgent() {
        return null;
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
            if (season.getYear()==seasonYear)
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
        return false;
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
    public boolean addTeam(Team Team) {
        return false;
    }

    @Override
    public ArrayList<Team> findTeamByName(String keywords) {
        return null;
    }

    @Override
    public ArrayList<Team> getAllTeams() {
        return null;
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

    }
}
