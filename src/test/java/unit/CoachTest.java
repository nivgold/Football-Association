package unit;


import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.Field;
import com.domain.logic.football.Team;
import com.domain.logic.roles.Coach;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import com.stubs.CoachInTeamStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CoachTest {

    private Coach c;
    private CoachInTeamStub cits;
    private Team t;

    @BeforeEach
    public void initiate() {
        AssociationSystem.getInstance().clearSystem();
        Address add = new Address("1", "1", "1,", "1");
        Member m = new Member("name", "1", "mail", add, "name");
        Member m2 = new Member("name2", "1", "mail", add, "name");
        this.c = new Coach(m);
        TeamOwner to = new TeamOwner(m2);
        Field f = new Field("Israel", "hasharon", "Herzliya", "12345");
        this.t = new Team("name", TeamStatus.Open, to, f);
        this.cits = new CoachInTeamStub("description", c, t);
    }

    @Test
    void addCoachInTeam() {
        assertFalse(t.getCoaches().contains(cits));
        c.addCoachInTeam(cits);
        t.addCoachInTeam(cits);
        assertTrue(t.getCoaches().contains(cits));
    }


    @Test
    void removeYourself() {
        assertTrue( c.getMember().getRoles().size() == 1);
        c.addCoachInTeam(cits);
        t.addCoachInTeam(cits);
        assertTrue(cits.getTeam().getCoaches().size() == 1);
        assertTrue(c.removeYourself());
        assertTrue(cits.getTeam().getCoaches().size() == 0);
        assertFalse(c.getMember().getRoles().contains(c));
    }
}