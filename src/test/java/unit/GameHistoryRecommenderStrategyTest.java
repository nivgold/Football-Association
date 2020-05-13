package unit;

import com.domain.logic.AssociationSystem;
import com.domain.logic.RecommenderSystem;
import com.domain.logic.recommender_system_strategies.GamesHistoryRecommenderStrategy;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.*;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameHistoryRecommenderStrategyTest {

    RecommenderSystem recommenderSystem = RecommenderSystem.getInstance();

    public RecommenderSystem getRecommenderSystem;
    League league = new League("league");
    Member member = new Member("test1","password","test@test",null,"yossi");
    TeamOwner teamOwner;

    {
        try {
            teamOwner = new TeamOwner(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Team team1 = new Team("team1", TeamStatus.Open,teamOwner,new Field("c","c","c","c"));
    Team team2 = new Team("team2", TeamStatus.Open,teamOwner,new Field("c","v","c","c"));
    Season season2019 = new Season(2019);
    Season season2020 = new Season(2020);
    SeasonInLeague seasonInLeague = new SeasonInLeague(league, season2019);
    Game game1 = new Game(team1,team2, seasonInLeague, LocalDateTime.now(),team1.getField());
    Game game2 = new Game(team1,team2,seasonInLeague,LocalDateTime.now(),team1.getField());



    @BeforeEach
    void init() {
        AssociationSystem.getInstance().clearSystem();
        recommenderSystem.BuildRecommenderSystem(new GamesHistoryRecommenderStrategy());
    }

    @Test
    void testGetChances() {
        game1.setHostTeamScore(2); //team1
        game1.setGuestTeamScore(1); //team2
        //team 1 won

        game2.setHostTeamScore(1); //team 1
        game2.setGuestTeamScore(0); //team 2
        //team 1 won

        season2019.addGame(game1);
        season2019.addGame(game2);

        SeasonInLeague seasonInLeague = new SeasonInLeague(league, season2019);

        Game game3 = new Game(team1,team2,seasonInLeague,LocalDateTime.now(),team1.getField());
        double chancesGame3 = recommenderSystem.getChances(game3);
        assertEquals(chancesGame3,1,"Team 1 100% against team 2 0%");
    }
}
