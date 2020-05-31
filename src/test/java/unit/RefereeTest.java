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
import com.domain.logic.utils.SHA1Function;
import com.stubs.DBStub;
import com.stubs.RefereeStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RefereeTest {

    private Member member;
    private Referee referee;
    private Player player;

    @BeforeEach
    public void initiate() throws Exception {
        AssociationSystem.getInstance().clearSystem();
        Address add = new Address("1", "1", "1,", "1");
        member = new Member("name", "1", "mail", add, "name");
        try {
            referee = new RefereeStub(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Game game = new Game(10, new ArrayList<>());
        Member member2 = new Member("asaf_player", SHA1Function.hash("asaf"), "asaf" , new Address("asaf", "asaf", "asaf", "asaf"), "asaf");
        player = new Player(member2);

        DBStub.getInstance().addMember(member);
        DBStub.getInstance().addGame(game);
        DBStub.getInstance().addMember(member2);
    }

    @Test
    public void createGameEvent() {
        // successfully adding new game event
        try {
            Event event = referee.createGameEvent(20, "blabla", EventType.Foul, 10, "Niv Team", "Tal Team", player.getMember().getUserName());
            assertEquals(10, event.getGameID());
            assertEquals(20, event.getGameMinute());
            assertEquals("blabla" ,event.getDescription());
            assertEquals("asaf_player" ,event.getPlayerUsername());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void createGameReport(){

    }
//
//    @Test
//    void editGameEvent() {
//        r.editGameEvent(g, oe, ne);
//        assertFalse(g.getEvents().contains(ne));
//        r.getMain().add(g);
//        r.editGameEvent(g, oe, ne);
//        assertTrue(g.getEvents().contains(ne));
//    }
//
//    @Test
//    void registerToGame() {
//        assertFalse(g.getRefereeObservers().contains(r));
//        r.registerToGame(g);
//        assertTrue(g.getRefereeObservers().contains(r));
//    }
//
//    @Test
//    void unregisterFromGame() {
//        r.registerToGame(g);
//        assertTrue(g.getRefereeObservers().contains(r));
//        r.unregisterFromGame(g);
//        assertFalse(g.getRefereeObservers().contains(r));
//    }
//
//    @Test
//    public void watchGameDetails() {
//        String ans = "";
//        String ans2 = r.watchGameDetails(g);
//        r.addMainGame(g);
//        ans = r.watchGameDetails(g);
//        assertTrue(ans.equals(g.toString()));
//        assertFalse(ans.equals(ans2));
//    }
//
//    @Test
//    public void getSchedulingDetails() {
//        String ans = "";
//        for (Game g : r.getMain()) {
//            if (g.getDate().isAfter(LocalDateTime.now())) {
//                ans += g.toString();
//            }
//        }
//        for (Game g : r.getSide()) {
//            if (g.getDate().isAfter(LocalDateTime.now())) {
//                ans += g.toString();
//            }
//        }
//        String ans2 = r.getSchedulingDetails();
//        assertTrue(ans.equals(ans2));
//    }
}