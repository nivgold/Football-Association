package unit;

import com.domain.logic.AssociationSystem;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.*;
import com.domain.logic.roles.Referee;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class GameTest {
    League league = new League("test league");
    Member member1 = new Member("Talfrim","123","x@x.x",null,"Tal");
    Member member2 = new Member("member2","123","x@x.x",null,"Tal");
    TeamOwner teamOwner1 = new TeamOwner(member1);
    Field filed1 = new Field("Israel","hasharon","Herzliya","12345");
    Field filed2 = new Field("Israel","hasharon","Herzliya","54321");
    Team team1 = new Team("team1-test", TeamStatus.Open,teamOwner1,filed1);
    Team team2 = new Team("team2-test",TeamStatus.Open,teamOwner1,filed2);
    Game game = new Game(team1,team2,new Season(2019),league, LocalDateTime.now(),filed1);
    Referee referee = new Referee(member2);



    @BeforeEach
    public void clean(){
        AssociationSystem.getInstance().clearSystem();
        game = new Game(team1,team2,new Season(2019),league, LocalDateTime.now(),filed1);
    }


    @Test
    void testRegister() {
        game.register(referee);
        assertTrue(game.getRefereeObservers().contains(referee));
        assertFalse(game.getFanObservers().contains(referee));

        game.register(member1);
        assertTrue(game.getFanObservers().contains(member1));
    }

    void testRemove() {
        game.register(referee);
        game.remove(referee);
        assertFalse(game.getRefereeObservers().contains(referee));

        game.register(member1);
        game.remove(member1);
        assertFalse(game.getFanObservers().contains(member1));
    }
}
