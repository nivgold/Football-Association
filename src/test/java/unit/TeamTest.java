package unit;


import com.domain.logic.AssociationSystem;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.*;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TeamTest {

    Field field = new Field("Israel", "hasharon", "Herzlya", "123");
    League league = new League("test league");
    Member member1 = new Member("Talfrim", "123", "x@x.x", null, "Tal");
    Member member2 = new Member("member2", "123", "x@x.x", null, "Tal");
    TeamOwner teamOwner1 = new TeamOwner(member1);
    Field filed1 = new Field("Israel", "hasharon", "Herzliya", "12345");
    Field filed2 = new Field("Israel", "hasharon", "Herzliya", "54321");
    Team team1 = new Team("team1-test", TeamStatus.Open, teamOwner1, filed1);
    Team team2 = new Team("team2-test", TeamStatus.Open, teamOwner1, filed2);
    Game game = new Game(team1, team2, new Season(2019), league, LocalDateTime.now(), filed1);

    @Test
    void testRemoveTeamPermanently() {
        AssociationSystem.getInstance().clearSystem();
        team1.removeTeamPermanently();
        assertTrue(team1.getStatus()==TeamStatus.PermanentlyClosed);
        assertTrue(team1.getPlayers().isEmpty());
        assertTrue(team1.getTeam_owners().isEmpty());
        assertTrue(team1.getCoachesInTeam().isEmpty());
        assertTrue(team1.getTeam_managers().isEmpty());
    }

    @Test
    void testRegister() {
        team1.register(teamOwner1);
        assertTrue(team1.getTeamObservers().contains(teamOwner1));
    }

    @Test
    void testRemove() {
        team1.register(teamOwner1);
        team1.remove(teamOwner1);
        assertFalse(team1.getTeamObservers().contains(teamOwner1));
    }

}