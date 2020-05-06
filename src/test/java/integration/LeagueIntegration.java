package integration;

import com.domain.logic.AssociationSystem;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.*;
import com.domain.logic.policies.Policy;
import com.domain.logic.roles.Referee;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;



public class LeagueIntegration{


    League league = new League("test league");
    Member member1 = new Member("Talfrim","123","x@x.x",null,"Tal");
    TeamOwner teamOwner1 = new TeamOwner(member1);
    Field filed1 = new Field("Israel","hasharon","Herzliya","12345");
    Field filed2 = new Field("Israel","hasharon","Herzliya","54321");
    Team team1 = new Team("team1-test", TeamStatus.Open,teamOwner1,filed1);
    Team team2 = new Team("team2-test", TeamStatus.Open,teamOwner1,filed2);
    Game game = new Game(team1,team2,new Season(2019),league, LocalDateTime.now(),filed1);
    Game game2 = new Game(team1,team2,new Season(2019),league, LocalDateTime.now(),filed1);
    Season season2019 = new Season(2019);
    Season season2020 = new Season(2020);
    Policy policy = new Policy(league,season2019);
    Referee referee = new Referee(member1);







    public LeagueIntegration() {

    }

    @BeforeEach
    public void clean() {
        AssociationSystem.getInstance().clearSystem();
        league = new League("test league");
    }

    @Test
    public void testAddGame() {
        league.addGame(game);
        assertTrue(game.getLeague()==league);
    }

    @Test
    void testAddSeason() {
        league.addSeason(season2019);
        assertTrue(season2019.getLeagues().contains(league));
    }

    @Test
    void testSetPolicyToSeason() throws Exception {
        //regular case
        league.addSeason(season2019);
        league.setPolicyToSeason(season2019,policy);
        assertEquals(season2019.getSeasonLeaguePolicy().get(league),policy);
    }


}


