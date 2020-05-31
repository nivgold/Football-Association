package unit;


import com.data.Dao;
import com.domain.logic.AssociationSystem;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.*;
import com.domain.logic.policies.GameSettingPolicy;
import com.domain.logic.policies.Policy;
import com.domain.logic.policies.RankingPolicy;
import com.domain.logic.policies.game_setting_policies.OneMatchEachPairSettingPolicy;
import com.domain.logic.policies.game_setting_policies.TwoMatchEachPairSettingPolicy;
import com.domain.logic.roles.Referee;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import com.stubs.DBStub;
import com.stubs.LeagueStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;



public class LeagueTest {


//    League league = new League("test league");
//    Member member1 = new Member("Talfrim","123","x@x.x",null,"Tal");
//    TeamOwner teamOwner1;
//
//    {
//        try {
//            teamOwner1 = new TeamOwner(member1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    Field filed1 = new Field("Israel","hasharon","Herzliya","12345");
//    Field filed2 = new Field("Israel","hasharon","Herzliya","54321");
//    Team team1 = new Team("team1-test", TeamStatus.Open,teamOwner1,filed1);
//    Team team2 = new Team("team2-test", TeamStatus.Open,teamOwner1,filed2);
//    SeasonInLeague seasonInLeague = new SeasonInLeague(league, new Season(2019));
//    Game game = new Game(team1,team2,seasonInLeague, LocalDateTime.now(),filed1);
//    Game game2 = new Game(team1,team2,seasonInLeague, LocalDateTime.now(),filed1);
//    Season season2019 = new Season(2019);
//    Season season2020 = new Season(2020);
//    SeasonInLeague seasonInLeague2 = new SeasonInLeague(league, season2019);
//    Policy policy = new Policy(seasonInLeague2);
//    Referee referee;
//
//    {
//        try {
//            referee = new Referee(member1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    private League league;
    private SeasonInLeague seasonInLeague;
    private Season season;
    public LeagueTest() {

    }

    @BeforeEach
    public void clean() {
        //AssociationSystem.getInstance().clearSystem();
        DBStub.getInstance().resetSystem();
        league = new LeagueStub("league");
        season = new Season(2011);
        seasonInLeague = new SeasonInLeague(league, season);
        DBStub.seasonInLeagues.add(seasonInLeague);
    }

    @Test
    public void testGetSeasonLeaguePolicy(){
        HashMap<Season, Policy> seasonLeaguePolicy = league.getSeasonLeaguePolicy();
        assertEquals(2, seasonLeaguePolicy.keySet().size());
        assertEquals(seasonInLeague.getPolicy(), seasonLeaguePolicy.get(season));
    }

    @Test
    public void testSetGameSettingPolicy() {
        GameSettingPolicy gameSettingPolicy = new GameSettingPolicy(seasonInLeague.getPolicy(), new OneMatchEachPairSettingPolicy());
        // setting game setting policy
        try {
            league.setGameSettingPolicy(season, gameSettingPolicy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            assertEquals(DBStub.getInstance().findSeasonInLeague(2011, "league").getPolicy().getGameSettingPolicy().getSettingStrategy().getClass(), OneMatchEachPairSettingPolicy.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetRankingPolicy(){
        RankingPolicy rankingPolicy = new RankingPolicy(seasonInLeague.getPolicy(), 1, 0, 0, 0, 0);
        // setting ranking policy
        try {
            league.setRankingPolicy(season, rankingPolicy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            RankingPolicy daoRankingPolicy = DBStub.getInstance().findSeasonInLeague(2011, "league").getPolicy().getRankingPolicy();
            assertEquals(daoRankingPolicy.getPolicy(), rankingPolicy.getPolicy());
            assertEquals(daoRankingPolicy.getWin(), rankingPolicy.getWin());
            assertEquals(daoRankingPolicy.getGoals(), rankingPolicy.getGoals());
            assertEquals(daoRankingPolicy.getDraw(), rankingPolicy.getDraw());
            assertEquals(daoRankingPolicy.getYellowCards(), rankingPolicy.getYellowCards());
            assertEquals(daoRankingPolicy.getRedCards(), rankingPolicy.getRedCards());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /*
    @Test
    public void testAddGame() {
        league.addGame(game);
        assertTrue(league.getGames().contains(game),"Adding first game");
        league.addGame(game2);
        assertTrue(league.getGames().contains(game2),"Adding second game");
    }

    @Test
    void testAddSeason() {
        league.addSeason(season2019);
        assertTrue(league.getSeasons().contains(season2019),"Adding first season");
        league.addSeason(season2020);
        assertTrue(league.getSeasons().contains(season2020),"Adding second season");
    }

    @Test
    void testSetPolicyToSeason() throws Exception {
        //trying with season isn't set up yet
        try {
            league.setPolicyToSeason(season2019,policy);
            assertFalse(true); //fails if not throwing an exception
        }
        catch (Exception e) {
            if (e.getMessage().equals("season not connected to league yet"))
                assertTrue(true);
            else
                assertFalse(true); //fail
        }

        //regular case
        league.addSeason(season2019);
        league.setPolicyToSeason(season2019,policy);
        assertEquals(league.getSeasonLeaguePolicy().get(season2019),policy);
    }

    @Test
    void testSetGameSettingPolicy() throws Exception {
        league.addSeason(season2019);
        league.setPolicyToSeason(season2019,policy);

        league.setGameSettingPolicy(season2019,"blabla");
        assertEquals(league.getSeasonLeaguePolicy().get(season2019).getGameSettingPolicy(),null);

        league.setGameSettingPolicy(season2019,"onematch");
        assertTrue(league.getSeasonLeaguePolicy().get(season2019).getGameSettingPolicy().getSettingStrategy() instanceof OneMatchEachPairSettingPolicy);

        league.setGameSettingPolicy(season2019,"twomatch");
        assertTrue(league.getSeasonLeaguePolicy().get(season2019).getGameSettingPolicy().getSettingStrategy() instanceof TwoMatchEachPairSettingPolicy);
    }

    @Test
    void testAddRefereeToSeasonInLeague() {
        assertFalse(league.addRefereeToSeasonInLeague(season2019,referee));

        league.addSeason(season2019);
        assertTrue(league.addRefereeToSeasonInLeague(season2019,referee)); //first adding return true
        assertTrue(league.addRefereeToSeasonInLeague(season2019,referee)); // already in return true
        assertTrue(league.getLeagueRefereeMap().get(season2019).contains(referee)); //adding succeeded
    }


     */

}


