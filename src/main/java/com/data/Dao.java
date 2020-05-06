package com.data;

import com.domain.logic.football.League;
import com.domain.logic.football.Season;
import com.domain.logic.football.Team;
import com.domain.logic.roles.*;
import com.domain.logic.users.Member;
import com.domain.logic.users.SystemManagerMember;

import java.util.ArrayList;

public interface Dao {
    // LEAGUE

    /**
     * @param leagueName
     * @return the league with that name
     */
    public League findLeague(String leagueName);

    /**
     * removes league from data
     * @param leagueName
     * @return boolean ans if successful
     */
    public boolean removeLeague(String leagueName);

    /**
     * @param league
     * @return a boolean answer that represent if it possible to add the given league.
     * if true it adds the league to the leagues hash.
     */
    public boolean addLeague(League league);

    /**
     * method that helps the search engine.
     * @param keywords
     * @return all leagues that has those keywords
     */
    public ArrayList<League> findLeagueByName(String keywords);

    /**
     * @return arrayList of all leagues in the system
     */
    public ArrayList<League> getAllLeagues();

    /**
     * removing all the leagues in the system
     */
    public void removeAllLeagues();

    // MEMBERS
    /**
     * @param userName
     * @param hashPassword
     * @return the member using those properties
     */
    public Member findMember(String userName, String hashPassword);

    /**
     * finding the given member in the DB
     * @param member
     * @return the correct member according to the given member
     */
    public Member findMember(Member member);

    /**
     * trying removing the member with the given userName and passwordHash
     * @param userName
     * @param passwordHash
     * @return True if the member was removed and false if not
     */
    public boolean removeMember(String userName, String passwordHash);

    /**
     * searching for members with the given name
     * @param name - member's name
     * @return list of members whose their name is the given name
     */
    public ArrayList<Member> findMemberByName(String name);

    /**
     * finding all of the correct members according to the given userName
     * @param userName
     * @return all the members with the given userName (Ignore case)
     */
    public ArrayList<Member> findMemberByUsername(String userName);

    /**
     * @param member
     * @return a boolean answer that represent if it possible to add the given member.
     * if true it adds the member to the members hash.
     */
    public boolean addMember(Member member);

    /**
     * @return all the coaches in the system
     */
    public ArrayList<Coach> findAllCoaches();

    /**
     * @return all the players in the system
     */
    public ArrayList<Player> findAllPlayers();

    /**
     * returns all of the referees in the DB
     * @return all of the referees in the DB
     */
    public ArrayList<Referee> findAllReferees();

    /**
     * returns all of the Team Owners in the DB
     * @return all of the Team Owners in the DB
     */
    public ArrayList<TeamOwner> findAllTeamOwner();

    /**
     * returns all of the Association Agents in the DB
     * @return all of the Association Agents in the DB
     */
    public ArrayList<AssociationAgent> findAllAssociationAgent();

    /**
     * returns all of the System Managers in the DB
     * @return all of the System Managers in the FB
     */
    public ArrayList<SystemManagerMember> findAllSystemManagers();

    /**
     * removing all the members for the DB
     */
    public void removeAllMembers();

    // SEASONS

    /**
     * @param seasonYear
     * @return the season in that year
     */
    public Season findSeason(Integer seasonYear);

    /**
     * removes season from data
     * @param seasonYear
     * @return boolean ans if successful
     */
    public boolean removeSeason(Integer seasonYear);

    /**
     * @param season
     * @return a boolean answer that represent if it possible to add the given season.
     * if true it adds the season to the season hash.
     */
    public boolean addSeason(Season season);

    /**
     * method that helps the search engine.
     * @param keywords
     * @return all seasons that has those keywords
     */
    public ArrayList<Season> findSeasonByYear(String keywords);

    /**
     * removing all the seasons from the DB
     */
    public void removeAllSeasons();

    // TEAMS
    /**
     * @param TeamName
     * @return the Business.football.Team with that name
     */
    public Team findTeam(String TeamName);

    /**
     * remove team in the DB with the given TeamName
     * @param TeamName
     * @return True if the team was removed and false if not
     */
    public boolean removeTeam(String TeamName);

    /**
     * @param Team
     * @return a boolean answer that represent if it possible to add the given Business.football.Team.
     * if true it adds the team to the teams hash.
     */
    public boolean addTeam(Team Team);

    /**
     * method that helps the search engine.
     * @param keywords
     * @return all Teams that has those keywords
     */
    public ArrayList<Team> findTeamByName(String keywords);

    /**
     * returns all the teams in the DB
     * @return all the teams in the DB
     */
    public ArrayList<Team> getAllTeams();

    /**
     * removing all the teams in the DB
     */
    public void removeAllTeams();
}
