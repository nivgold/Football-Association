package unit;


import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.enums.PlayerRole;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.Field;
import com.domain.logic.football.Team;
import com.domain.logic.roles.Player;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import com.stubs.PlayerRoleInTeamStub;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {

    static Player p;
    static PlayerRoleInTeamStub prits;


    @BeforeAll
    public static void initiate() {
        AssociationSystem.getInstance().clearSystem();
        Address add = new Address("1", "1", "1,", "1");
        Member m = new Member("name", "1", "mail", add, "name");
        Member m2 = new Member("name2", "1", "mail", add, "name");
        Date d = new Date();
        p = new Player( m, d);
        TeamOwner to = new TeamOwner(m2);
        Field f = new Field("Israel", "hasharon", "Herzliya", "12345");
        Team t = new Team("name", TeamStatus.Open, to, f);
        prits = new PlayerRoleInTeamStub(p, t, PlayerRole.CAM);
        t.addPlayerRoleInTeam(prits);
        p.addPlayerRoleInTeam(prits);
    }

    @Test
    public void removeYourself() {
        assertTrue( p.getMember().getRoles().size() == 1);
        assertTrue(prits.getTeam().getPlayers().size() == 1);
        assertTrue(p.removeYourself());
        assertTrue(prits.getTeam().getPlayers().size() == 0);
        assertFalse(p.getMember().getRoles().contains(p));
    }
}