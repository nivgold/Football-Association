package com.data;
import com.domain.logic.data_types.Address;
import com.domain.logic.data_types.GameIdentifier;
import com.domain.logic.enums.EventType;
import com.domain.logic.football.*;
import com.domain.logic.policies.game_setting_policies.OneMatchEachPairSettingPolicy;
import com.domain.logic.policies.game_setting_policies.TwoMatchEachPairSettingPolicy;
import com.domain.logic.roles.*;
import com.domain.logic.users.Member;
import com.domain.logic.users.SystemManagerMember;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.stereotype.Repository;
import java.io.*;
import java.sql.*;
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
    public boolean checkIfTeamExists(String teamName) throws Exception {
        Connection connection = DBConnector.getConnection();
        String sql = "SELECT * FROM team WHERE teamName LIKE ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, teamName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return true;
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("SQL exception");
        }
    }
    @Override
    public Game findGame(int gameID){
        return null;
    }
    @Override
    public boolean hasTeam(String teamOwnerUsername) throws Exception {
        Connection connection = DBConnector.getConnection();
        String sql = "SELECT team_owner.* FROM member INNER JOIN team_owner ON " +
                "team_owner.memberID=member.memberID " +
                "WHERE member.username = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, teamOwnerUsername);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                if (resultSet.getInt("teamID")!=0)
                    return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("SQL exception");
        }
    }
    @Override
    public SeasonInLeague findSeasonInLeague(int seasonYear, String leagueName) throws Exception {
        Connection connection = DBConnector.getConnection();
        String sql = "SELECT seasonYear, league_name, PolicyID FROM seasoninleague" +
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
                int policyID = resultSet.getInt("policyID");
                String getPolicy = "SELECT * FROM policy" +
                        " WHERE policyID = ?";
                PreparedStatement getPolicyStatement = connection.prepareStatement(getPolicy);
                getPolicyStatement.setInt(1, policyID);
                resultSet = getPolicyStatement.executeQuery();
                if(resultSet.next()){
                    boolean gameSett = resultSet.getBoolean("gameSettingPolicy");
                    if(gameSett){
                        seasonInLeague.getPolicy().setGameSettingPolicy(new TwoMatchEachPairSettingPolicy());
                    }
                    else{
                        seasonInLeague.getPolicy().setGameSettingPolicy(new OneMatchEachPairSettingPolicy());
                    }
                    int rankSett = resultSet.getInt("rankingPolicyID");
                    if(rankSett != 0){
                        String getRankingFields = "SELECT * FROM rankingpolicy" +
                                " WHERE rankingPolicyID = ?";
                        PreparedStatement getRankingStatement = connection.prepareStatement(getRankingFields);
                        getRankingStatement.setInt(1, rankSett);
                        resultSet = getRankingStatement.executeQuery();
                        if(resultSet.next()){
                        int win = resultSet.getInt("win");
                        int goals = resultSet.getInt("goals");
                        int draw = resultSet.getInt("draw");
                        int yellowCards = resultSet.getInt("yellowCards");
                        int redCards = resultSet.getInt("redCards");
                        seasonInLeague.getPolicy().setRankingPolicy(win, goals, draw, yellowCards, redCards);
                        }
                    }
                    else{
                        //insert new value to ranking policy table
                        insertRankingPolicy(connection, 3, 2, 1, 1, 3, policyID);
                        seasonInLeague.getPolicy().setRankingPolicy(3, 1, 1, 1, 2);
                    }
                }

            }
            connection.close();
            return seasonInLeague;
        } catch (SQLException e) {
            connection.close();
            throw new Exception("cannot perform operation");
        }
    }

    /**
     * insert new value to ranking policy table and update the policy table as well
     */
    private void insertRankingPolicy(Connection connection, int win, int goals, int draw, int yellowCards, int redCards, int policyID) {
        String setDefRanking = "INSERT INTO rankingpolicy (win, goals, draw, yellowCards, redCards, policyID) " +
                "   VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement setDefRankingStatement = connection.prepareStatement(setDefRanking, Statement.RETURN_GENERATED_KEYS);
            setDefRankingStatement.setInt(1, win);
            setDefRankingStatement.setInt(2, goals);
            setDefRankingStatement.setInt(3, draw);
            setDefRankingStatement.setInt(4, yellowCards);
            setDefRankingStatement.setInt(5, redCards);
            setDefRankingStatement.setInt(6, policyID);
            setDefRankingStatement.execute();
            ResultSet resultSet = setDefRankingStatement.getGeneratedKeys();
            int rankingPolicyID;
            if (resultSet.next()){
                rankingPolicyID = resultSet.getInt(1);
                //update policy
                String updatePolicy = "UPDATE policy " +
                        " SET rankingPolicyID = ? " +
                        " WHERE policyID = ?";
                PreparedStatement updatePolicyStatement = connection.prepareStatement(updatePolicy);
                updatePolicyStatement.setInt(1, rankingPolicyID);
                updatePolicyStatement.setInt(2, policyID);
                updatePolicyStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            int policyID;
            int rankingPolicyID;
            // find PolicyID Query
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT p.policyID, p.rankingPolicyID  " +
                    "FROM seasoninleague INNER JOIN policy p " +
                    "ON seasoninleague.leagueID = p.leagueID AND seasoninleague.seasonYear = p.seasonYear " +
                    "INNER JOIN league l on seasoninleague.leagueID = l.leagueID" +
                    " WHERE seasoninleague.seasonYear = ? AND l.league_name LIKE ?");
            preparedStatement.setInt(1, seasonYear);
            preparedStatement.setString(2, leagueName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                policyID = resultSet.getInt(1);
                rankingPolicyID = resultSet.getInt(2);
                if(rankingPolicyID == 0){
                    // need to add new ranking policy ID
                    insertRankingPolicy(connection, win, goals, draw, yellowCards, redCards, policyID);
                }
                else {
                    //need to update existing ranking policy
                    updateRankingPolicy(connection, win, goals, draw, yellowCards, redCards, rankingPolicyID);
                }
            }
            connection.close();
        } catch (SQLException e) {
            connection.close();
            throw new Exception("cannot perform operation");
        }
    }

    private void updateRankingPolicy(Connection connection, int win, int goals, int draw, int yellowCards, int redCards, int rankingPolicyID) {
        String updateRanking = "UPDATE rankingpolicy " +
                "SET win = ?, goals = ?, draw = ?, yellowCards = ?, redCards = ? " +
                "WHERE rankingPolicyID = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(updateRanking);
            preparedStatement.setInt(1, win);
            preparedStatement.setInt(2, goals);
            preparedStatement.setInt(3, draw);
            preparedStatement.setInt(4, yellowCards);
            preparedStatement.setInt(5, redCards);
            preparedStatement.setInt(6, rankingPolicyID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameIdentifier getRefereeActiveGame(String refereeUsername) throws Exception {
        Connection connection = DBConnector.getConnection();
        String sql = "SELECT game.gameID, t.teamName as guestName, t2.teamName as hostName FROM referee INNER JOIN member ON " +
                "referee.memberID = member.memberID INNER JOIN side_referee_in_game ON " +
                "side_referee_in_game.side_referee_id = referee.refereeID INNER JOIN game ON " +
                "game.gameID = side_referee_in_game.gameID INNER JOIN team t ON game.guest_teamID = t.teamID " +
                "INNER JOIN team t2 ON t2.teamID = game.host_teamID " +
                "WHERE member.username LIKE ? AND NOW() BETWEEN game.date AND DATE_ADD(game.date, INTERVAL 100 MINUTE)";
        try{
            GameIdentifier gameIdentifier = null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, refereeUsername);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                int gameID = resultSet.getInt("gameID");
                String guestName = resultSet.getString("guestName");
                String hostName = resultSet.getString("hostName");
                gameIdentifier = new GameIdentifier(gameID, hostName, guestName);
            }
            return gameIdentifier;
        } catch (SQLException e) {
            throw new Exception("SQL exception");
        }
    }

    @Override
    public ArrayList<String> getAllTeamPlayers(String teamName) throws Exception {
        Connection connection = DBConnector.getConnection();
        String sql = "SELECT member.username FROM member INNER JOIN player ON player.memberID=member.memberID " +
                "INNER JOIN player_in_team pit on player.playerID = pit.playerID " +
                "INNER JOIN team t on pit.teamID = t.teamID " +
                "WHERE t.teamName LIKE ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, teamName);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> teamPlayersNames = new ArrayList<>();
            while (resultSet.next()){
                teamPlayersNames.add(resultSet.getString(1));
            }
            return teamPlayersNames;
        } catch (SQLException e) {
            throw new Exception("SQL exception");
        }
    }

    @Override
    public void addGameEvent(int gameID, int gameMinute, String description, EventType type, String playerUsername, int changeScore) throws Exception {
        Connection connection = DBConnector.getConnection();
        String sql_create_event = "INSERT INTO event (game_minute, description, event_type, playerID, gameID) " +
                "VALUES (?, ?, ?, (SELECT player.playerID FROM player INNER JOIN member m on player.memberID = m.memberID WHERE m.username LIKE ?), ?)";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql_create_event);
            preparedStatement.setInt(1, gameMinute);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, type.ordinal());
            preparedStatement.setString(4, playerUsername);
            preparedStatement.setInt(5, gameID);
            preparedStatement.executeUpdate();

            if (changeScore != 0){
                String sql_change_score;
                if (changeScore == -1)
                    sql_change_score = "UPDATE game " +
                            "SET guest_team_score = guest_team_score + 1 " +
                            "WHERE gameID = ?";
                else
                    sql_change_score = "UPDATE game " +
                            "SET host_team_score = host_team_score + 1 " +
                            "WHERE gameID = ?";
                preparedStatement = connection.prepareStatement(sql_change_score);
                preparedStatement.setInt(1, gameID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new Exception("SQL exception");
        }
    }

    @Override
    public void setGameReport(int gameID, String report) throws Exception {
        Connection connection = DBConnector.getConnection();
        String sql = "UPDATE game " +
                "SET report = ? " +
                "WHERE gameID = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, report);
            preparedStatement.setInt(2, gameID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("SQL exception");
        }
    }

    @Override
    public boolean isRefereeAuthorized(String refereeUsername, int gameID) throws Exception {
        Connection connection = DBConnector.getConnection();
        String sql = "(SELECT g.gameID FROM member INNER JOIN referee r on member.memberID = r.memberID\n" +
                "    INNER JOIN game g on r.refereeID = g.main_refereeID\n" +
                "    WHERE g.gameID = ? AND member.username LIKE ? \n" +
                "    AND NOW() BETWEEN g.date AND DATE_ADD(g.date, INTERVAL 100 MINUTE ))\n" +
                "UNION\n" +
                "(SELECT g2.gameID FROM member INNER JOIN referee on referee.memberID = member.memberID\n" +
                "    INNER JOIN side_referee_in_game srig on referee.refereeID = srig.side_referee_id\n" +
                "    INNER JOIN game g2 on srig.gameID = g2.gameID\n" +
                "    WHERE g2.gameID = ? AND member.username LIKE ? \n" +
                "    AND NOW() BETWEEN g2.date AND DATE_ADD(g2.date, INTERVAL 100 MINUTE ))";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, gameID);
            preparedStatement.setString(2, refereeUsername);
            preparedStatement.setInt(3, gameID);
            preparedStatement.setString(4, refereeUsername);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new Exception("SQL exception");
        }
    }

    @Override
    public boolean isReportAuthorized(String refereeUsername, int gameID) throws Exception {
        Connection connection = DBConnector.getConnection();
        String sql = "SELECT gameID FROM game " +
                "INNER JOIN referee r on game.main_refereeID = r.refereeID " +
                "INNER JOIN member m on r.memberID = m.memberID " +
                "WHERE game.gameID = ? AND m.username LIKE ? " +
                "AND NOW() BETWEEN DATE_ADD(game.date, INTERVAL 100 MINUTE ) AND DATE_ADD(game.date, INTERVAL 400 MINUTE )";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, gameID);
            preparedStatement.setString(2, refereeUsername);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new Exception("SQL exception");
        }
    }

    @Override
    public ArrayList<String> getAllTeamNames() throws Exception {
        Connection connection = DBConnector.getConnection();
        String sql = "SELECT teamName FROM team";
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ArrayList<String> teamNames = new ArrayList<>();
            while (resultSet.next())
                teamNames.add(resultSet.getString("teamName"));
            return teamNames;
        } catch (SQLException e) {
            throw new Exception("SQL exception");
        }
    }

    @Override
    public ArrayList<String[]> getGameFans(int gameID) throws Exception {
        Connection connection = DBConnector.getConnection();
        String sql = "SELECT username, email FROM gamefans " +
                "INNER JOIN member m on gamefans.memberID = m.memberID " +
                "WHERE gameID = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, gameID);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String[]> members = new ArrayList<>();
            while (resultSet.next()){
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                members.add(new String[]{username, email});
            }
            return members;
        } catch (SQLException e) {
            throw new Exception("SQL exception");
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
    public ArrayList<String> getAllLeaguesNames() throws Exception {
        Connection connection = DBConnector.getConnection();
        String sql ="SELECT league_name FROM league";
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ArrayList<String> leagues = new ArrayList<>();
            while (resultSet.next()){
                leagues.add(resultSet.getString("league_name"));
            }
            return leagues;
        } catch (SQLException e) {
            throw new Exception("cannot perform operation");
        }
    }

    @Override
    public void removeAllLeagues() {

    }

    @Override
    public Member findMember(String username) throws Exception {
        Connection connection = DBConnector.getConnection();
        String sql = "SELECT * FROM member INNER JOIN" +
                " address ON address.addressID=member.addressID " +
                " WHERE username LIKE ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            Member member = null;
            while (resultSet.next()){
                String userName = resultSet.getString("username");
                String passwordHash = resultSet.getString("passwordHash");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                String state = resultSet.getString("state");
                String city = resultSet.getString("city");
                String postalCode = resultSet.getString("postalCode");
                Address address = new Address(country, state, city, postalCode);
                String name = resultSet.getString("name");
                boolean sysManager = resultSet.getBoolean("systemManager");
                // has member
                if(sysManager){
                    member = new SystemManagerMember(username, passwordHash, email, address, name);
                }
                else{
                    member = new Member(username, passwordHash, email, address, name);
                }
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
                boolean sysManager = resultSet.getBoolean("systemManager");
                // has member
                if(sysManager){
                    member = new SystemManagerMember(username, passwordHash, email, address, name);
                }
                else{
                    member = new Member(username, passwordHash, email, address, name);
                }

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
    public void addTeam(String teamName, String country, String state, String city, String postalCode, TeamOwner teamOwner) throws Exception {
        Connection connection = DBConnector.getConnection();
        String sql = "SELECT teamOwnerID FROM member" +
                " WHERE username = ?";
        try {
            //first create new team
            Member member = teamOwner.getMember();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, member.getUserName());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int teamOwnerID = resultSet.getInt(1);
                //create team
                String queryInsertTeam = "INSERT INTO team (teamName) VALUES (?)";
                PreparedStatement statementInsertTeam = connection.prepareStatement(queryInsertTeam, Statement.RETURN_GENERATED_KEYS);
                statementInsertTeam.setString(1, teamName);
                statementInsertTeam.executeUpdate();
                int teamID;
                ResultSet rs = statementInsertTeam.getGeneratedKeys();
                if (rs.next()){
                    teamID = rs.getInt(1);
                }
                else
                    throw new Exception("new team wan't added properly");
                //create address
                String queryInsertAddress = "INSERT INTO address (country, state, city, postalCode) VALUES (?,?,?,?)";
                PreparedStatement statementInsertAddress = connection.prepareStatement(queryInsertAddress, Statement.RETURN_GENERATED_KEYS);
                statementInsertAddress.setString(1, country);
                statementInsertAddress.setString(2, state);
                statementInsertAddress.setString(3, city);
                statementInsertAddress.setString(4, postalCode);
                statementInsertAddress.executeUpdate();
                int fieldAddressID;
                rs = statementInsertAddress.getGeneratedKeys();
                if (rs.next()){
                    fieldAddressID = rs.getInt(1);
                }
                else
                    throw new Exception("new address for the team's field wan't added properly");
                //create field
                String queryInsertField = "INSERT INTO field (teamID, addressID) VALUES (?,?)";
                PreparedStatement statementInsertField = connection.prepareStatement(queryInsertField, Statement.RETURN_GENERATED_KEYS);
                statementInsertField.setInt(1, teamID);
                statementInsertField.setInt(2, fieldAddressID);
                statementInsertField.executeUpdate();
                int fieldID;
                rs = statementInsertField.getGeneratedKeys();
                if (rs.next()){
                    fieldID = rs.getInt(1);
                }
                else
                    throw new Exception("new field for the team wan't added properly");
                //update team
                String queryUpdateTeam = "UPDATE team " +
                        "SET team.fieldID = ? " +
                        "WHERE teamID = ?";
                PreparedStatement statementUpdateTeam = connection.prepareStatement(queryUpdateTeam);
                statementUpdateTeam.setInt(1, fieldID);
                statementUpdateTeam.setInt(2, teamID);
                statementUpdateTeam.executeUpdate();
                //update teamOwner
                String queryUpdateTeamOwner = "UPDATE team_owner " +
                        "SET teamID = ? " +
                        "WHERE teamOwnerID = ?";
                PreparedStatement statementUpdateTeamOwner = connection.prepareStatement(queryUpdateTeamOwner);
                statementUpdateTeamOwner.setInt(1, teamID);
                statementUpdateTeamOwner.setInt(2, teamOwnerID);
                statementUpdateTeamOwner.executeUpdate();
            }
            connection.close();
        } catch (SQLException e) {
            connection.close();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void resetSystem() {
        Connection connection = DBConnector.getConnection();
        ScriptRunner runner=new ScriptRunner(connection);
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(new File("src\\main\\java\\com\\data\\sql_scripts\\footballassociationdb_schema_create.sql")));
            runner.runScript(reader);
            reader.close();
            connection.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
