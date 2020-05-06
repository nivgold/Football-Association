package unit;


import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.enums.EventType;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.*;
import com.domain.logic.roles.Player;
import com.domain.logic.roles.Referee;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RefereeTest {

    private Member m;
    private Referee r;
    private Game g;
    private Team t;
    private Season s;
    private League l;
    private Field f;
    private TeamOwner to;
    private Player p;
    private Date d;
    private Event ne;
    private Event oe;

    @BeforeEach
    public void initiate() {
        AssociationSystem.getInstance().clearSystem();
        Address add = new Address("1", "1", "1,", "1");
        m = new Member("name", "1", "mail", add, "name");
        r = new Referee(m);
        d = new Date();
        p = new Player( m, d);
        to = new TeamOwner(m);
        f = new Field("1", "1", "1,", "1");
        t = new Team("team", TeamStatus.Open, to, f);
        s = new Season(2020);
        l = new League("league");
        g = new Game(t, t, s, l, LocalDateTime.now(), f);
        ne = new Event(1, "a", EventType.Foul, g, p);
        oe = new Event(2, "a", EventType.Foul, g, p);
    }

    @Test
    void createGameEvent() {
        r.createGameEvent(1, "e", EventType.Foul, d, g, p);
        assertTrue(g.getEvents().size() == 0);
        r.getMain().add(g);
        r.createGameEvent(1, "e", EventType.Foul, d, g, p);
        assertTrue(g.getEvents().size() == 1);
    }

    @Test
    void editGameEvent() {
        r.editGameEvent(g, oe, ne);
        assertFalse(g.getEvents().contains(ne));
        r.getMain().add(g);
        r.editGameEvent(g, oe, ne);
        assertTrue(g.getEvents().contains(ne));
    }

    @Test
    void registerToGame() {
        assertFalse(g.getRefereeObservers().contains(r));
        r.registerToGame(g);
        assertTrue(g.getRefereeObservers().contains(r));
    }

    @Test
    void unregisterFromGame() {
        r.registerToGame(g);
        assertTrue(g.getRefereeObservers().contains(r));
        r.unregisterFromGame(g);
        assertFalse(g.getRefereeObservers().contains(r));
    }

    @Test
    public void watchGameDetails() {
        String ans = "";
        String ans2 = r.watchGameDetails(g);
        r.addMainGame(g);
        ans = r.watchGameDetails(g);
        assertTrue(ans.equals(g.toString()));
        assertFalse(ans.equals(ans2));
    }

    @Test
    public void getSchedulingDetails() {
        String ans = "";
        for (Game g : r.getMain()) {
            if (g.getDate().isAfter(LocalDateTime.now())) {
                ans += g.toString();
            }
        }
        for (Game g : r.getSide()) {
            if (g.getDate().isAfter(LocalDateTime.now())) {
                ans += g.toString();
            }
        }
        String ans2 = r.getSchedulingDetails();
        assertTrue(ans.equals(ans2));
    }
}