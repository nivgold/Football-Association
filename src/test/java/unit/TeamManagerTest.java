package unit;


import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.Field;
import com.domain.logic.roles.TeamManager;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import com.domain.logic.utils.SHA1Function;
import com.stubs.TeamStub;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class TeamManagerTest {

    static TeamManager tm;
    static TeamOwner to;
    static TeamStub t;
    static Member m;
    static Member m2;


    @BeforeAll
    public static void initiate() {
        AssociationSystem.getInstance().clearSystem();
        m = new Member("owner", SHA1Function.hash("owner"), "owner@gmail.com", new Address("Israel", "Israel", "Haifa", "3189240"), "moshe");
        m2 = new Member("manager", SHA1Function.hash("manager"), "manager@gmail.com", new Address("Israel", "Israel", "Haifa", "3189240"), "moshe");
        to = new TeamOwner(m);
        // creating a team for the team owner
        t = new TeamStub("Hapoel Beer Sheva", TeamStatus.Open, to, new Field("Israel", "Israel", "Haifa", "2018756"));
        t.teamOwners.add(to);
        to.setTeam(t);

    }

    @Test
    void removeYourself() {
        assertFalse(t.getTeam_managers().contains(tm));
        to.appointTeamManager(m2);
        tm = t.getTeam_managers().get(0);
        assertTrue(t.getTeam_managers().contains(tm));
        assertTrue(tm.removeYourself());
        assertFalse(t.getTeam_managers().contains(tm));
    }
}