package com.data;


import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.football.*;
import com.domain.logic.roles.*;
import com.domain.logic.users.Member;
import com.domain.logic.users.SystemManagerMember;
import org.springframework.stereotype.Repository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    @Override
    public void addLog(String data) throws SQLException {
        Connection connection = DBConnector.getConnection();
        String sql ="INSERT INTO log (data) " +
                "VALUES (?)";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, data);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            connection.close();
        }
    }
    @Override
    public SeasonInLeague findSeasonInLeague(int seasonYear, String leagueName) throws Exception {
        Connection connection = DBConnector.getConnection();
        String sql = "SELECT seasonYear, league_name FROM seasoninleague" +
                " INNER JOIN league ON  league.leagueID = seasoninleague.leagueID" +
                " WHERE seasonYear = ? AND  league_name LIKE ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, seasonYear);
            preparedStatement.setString(2, leagueName);
            ResultSet resultSet = preparedStatement.executeQuery();
            SeasonInLeague seasonInLeague = null;

            while (resultSet.next()){
                int year = resultSet.getInt("seasonYear");
                Season season = new Season(year);
                String league_name = resultSet.getString("league_name");
                League league = new League(league_name);
                seasonInLeague = new SeasonInLeague(league, season);
            }
            connection.close();
            return seasonInLeague;
        } catch (SQLException e) {
            connection.close();
            throw new Exception("cannot perform operation");
        }
    }
    @Override
    public void setGameSettingPolicy(int seasonYear, String leagueName, boolean gameSettingPolicyField) throws Exception {
        Connection connection = DBConnector.getConnection();
        String sql = "UPDATE policy" +
                " SET gameSettingPolicy = ?" +
                " WHERE policyID = (SELECT PolicyID FROM seasoninleague INNER JOIN league ON league.leagueID=seasoninleague.leagueID" +
                " WHERE seasoninleague.seasonYear = ? AND league.league_name LIKE ?)";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, gameSettingPolicyField);
            preparedStatement.setInt(2, seasonYear);
            preparedStatement.setString(3, leagueName);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            connection.close();
            throw new Exception("cannot perform operation");
        }
    }

    @Override
    public void setGameRankingPolicy(int seasonYear, String leagueName, int win, int goals, int draw, int yellowCards, int redCards) throws Exception {
        Connection connection = DBConnector.getConnection();
        try{
            int policyID = -1;

            // find PolicyID Query
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT PolicyID FROM seasoninleague INNER JOIN league ON league.leagueID=seasoninleague.leagueID WHERE seasoninleague.seasonYear = ? AND league.league_name LIKE ?");
            preparedStatement.setInt(1, seasonYear);
            preparedStatement.setString(2, leagueName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                policyID = resultSet.getInt("policyID");

            // find rankingPolicyID Query
            String sql_find = "SELECT rankingPolicyID FROM rankingpolicy" +
                    " WHERE draw = ? AND" +
                    " goals = ? AND" +
                    " redCards = ? AND" +
                    " win = ? AND" +
                    " yellowCards = ?";
            preparedStatement = connection.prepareStatement(sql_find);
            preparedStatement.setInt(1, draw);
            preparedStatement.setInt(2, goals);
            preparedStatement.setInt(3, redCards);
            preparedStatement.setInt(4, win);
            preparedStatement.setInt(5, yellowCards);
            resultSet = preparedStatement.executeQuery();

            int rankingPolicyID;
            if (resultSet.next()){
                rankingPolicyID = resultSet.getInt("rankingPolicyID");
            }
            else{
                // need to add ranking policy ID
                preparedStatement.executeUpdate("INSERT INTO rankingpolicy (win, goals, draw, yellowCards, redCards, policyID) " +
                        "VALUE (?, ?, ?, ?, ?, policyID)");
                preparedStatement.setInt(1, win);
                preparedStatement.setInt(2, goals);
                preparedStatement.setInt(3, draw);
                preparedStatement.setInt(4, yellowCards);
                preparedStatement.setInt(5, redCards);
                preparedStatement.executeUpdate();
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()){
                    rankingPolicyID = rs.getInt(1);
                }
                else
                    throw new Exception("new ranking policy wan't added properly");
            }

            // has valid ranking policy id
            // need to connect to the policy
            String sqlUpdatePolicy = "UPDATE policy " +
                    "SET rankingPolicyID = ? " +
                    "WHERE policyID = ?";
            preparedStatement = connection.prepareStatement(sqlUpdatePolicy);
            preparedStatement.setInt(1, rankingPolicyID);
            preparedStatement.setInt(2, policyID);
            connection.close();
        } catch (SQLException e) {
            connection.close();
            throw new Exception("cannot perform operation");
        }
    }
    // -------------added functions---------------

    @Override
    public League findLeague(String leagueName) throws Exception {
        Connection connection = DBConnector.getConnection();
        String sql = "SELECT league_name FROM league " +
                "WHERE leagueID LIKE ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, leagueName);
            ResultSet resultSet = preparedStatement.executeQuery();
            League league = null;
            while (resultSet.next()){
                String league_name = resultSet.getString("league_name");
                league = new League(league_name);
            }
            connection.close();
            return league;
        } catch (SQLException e) {
            connection.close();
            throw new Exception("cannot create prepared statement");
        }
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
    public Member findMember(String userName, String hashPassword) throws Exception {
        Connection connection = DBConnector.getConnection();
        String sql = "SELECT * FROM member INNER JOIN" +
                " address ON address.addressID=member.addressID " +
                " WHERE username = ? and passwordHash = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, hashPassword);
            ResultSet resultSet = preparedStatement.executeQuery();
            Member member = null;
            while (resultSet.next()){
                String username = resultSet.getString("username");
                String passwordHash = resultSet.getString("passwordHash");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                String state = resultSet.getString("state");
                String city = resultSet.getString("city");
                String postalCode = resultSet.getString("postalCode");
                Address address = new Address(country, state, city, postalCode);
                String name = resultSet.getString("name");
                // has member
                member = new Member(username, passwordHash, email, address, name);

                // check for coach
                int coachID = resultSet.getInt("coachID");
                if (coachID != 0) new Coach(member);
                // check for team owner
                int teamOwnerID = resultSet.getInt("teamOwnerID");
                if (teamOwnerID !=0 ) new TeamOwner(member);
                // check for team manager
                int teamManagerID = resultSet.getInt("teamManagerID");
                if (teamManagerID !=0 ) new TeamManager(member);
                // check for player
                int playerID = resultSet.getInt("playerID");
                if (playerID !=0 ) new Player(member);
                // check for referee
                int refereeID = resultSet.getInt("refereeID");
                if (refereeID !=0 ) new Referee(member);
                // check for association agent
                int associationAgentID = resultSet.getInt("associationAgentID");
                if (associationAgentID !=0 ) new AssociationAgent(member);
            }
            connection.close();
            return member;
        } catch (SQLException e) {
            connection.close();
            throw new Exception("SQL exception");
        }
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
        Connection connection = DBConnector.getConnection();
        String sql = "SELECT seasonYear FROM season " +
                "WHERE seasonYear = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, seasonYear);
            ResultSet resultSet = preparedStatement.executeQuery();
            Season season = null;
            while (resultSet.next()){
                season = new Season(resultSet.getInt("seasonYear"));
            }
            connection.close();
            return season;
        } catch (SQLException e) {
            connection.close();
            throw new Exception("cannot create prepared statement");
        }
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
