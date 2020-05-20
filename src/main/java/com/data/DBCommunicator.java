package com.data;


import com.domain.logic.AssociationSystem;
import com.domain.logic.football.Game;
import com.domain.logic.football.League;
import com.domain.logic.football.Season;
import com.domain.logic.football.Team;
import com.domain.logic.roles.*;
import com.domain.logic.users.Member;
import com.domain.logic.users.SystemManagerMember;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;

@Repository("DBCommunicator")
public class DBCommunicator implements Dao {

    private static DBCommunicator dbCommunicator;

    private DBCommunicator(){

    }

    public static DBCommunicator getInstance(){
        if (dbCommunicator == null)
            dbCommunicator = new DBCommunicator();

        return dbCommunicator;
    }

    // -------------added functions---------------
    @Override
    public boolean checkIfTeamExists(String teamName){
        return false;
    }
    @Override
    public Game findGame(int gameID){
        return null;
    }
    @Override
    public Player findPlayer(int playerID){
        return null;
    }
    @Override
    public boolean hasTeam(String teamOwnerUsername, String teamName){
        return false;
    }
    // -------------added functions---------------

    @Override
    public League findLeague(String leagueName) {

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
    public ArrayList<League> getAllLeagues() {
        return null;
    }

    @Override
    public void removeAllLeagues() {

    }

    @Override
    public Member findMember(String userName, String hashPassword) {
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
    public Season findSeason(Integer seasonYear) {
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
}
