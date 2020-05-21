package com.data;


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
                int coachID = resultSet.getInt("couchID");
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
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void addTeam(String teamName, Address field, TeamOwner teamOwner) throws Exception {
        Connection connection = DBConnector.getConnection();
        String sql = "SELECT teamOwnerID FROM member INNER JOIN" +
                " WHERE username = ?";
        try {
            //first create new team
            Member member = teamOwner.getMember();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, member.getName());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){

                //TODO create team
                String sql2 = "INSERT INTO team (teamName) VALUES (?)";
                PreparedStatement preparedStatement1 = connection.prepareStatement(sql2);
                preparedStatement1.setString(1, teamName);

                int teamOwnerID = resultSet.getInt("teamOwnerID");
            }
            connection.close();
        } catch (SQLException e) {
            connection.close();
            throw new Exception(e.getMessage());
        }
    }



    //LIAR LIAR PANTS ON FIRE:
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
