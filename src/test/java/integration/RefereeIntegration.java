package integration;

import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.enums.EventType;
import com.domain.logic.enums.PlayerRole;
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
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


class RefereeIntegration {

    private Game game;
    private Referee referee;
    private AssociationSystem system;
    private DBStub dbStub = DBStub.getInstance();

    @BeforeEach
    public void beforeTestMethod(){
        AssociationSystem.getInstance().clearSystem();
        Member member = new Member("referee", SHA1Function.hash("referee"), "referee@gmail.com", new Address("Israel", "Israel", "Haifa", "6127824"), "shimon");
        dbStub.addMember(member);
        try {
            this.referee = new RefereeStub(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.system = AssociationSystem.getInstance();

        League league = new League("league1");
        dbStub.addLeague(league);
        Season season = new Season(2012);
        dbStub.addSeason(season);
        league.addSeason(season);
        Member member2 = new Member("owner", SHA1Function.hash("owner"), "owner@gmail.com", new Address("Israel", "Israel", "Haifa", "3189240"), "moshe");
        dbStub.addMember(member2);
        TeamOwner teamOwner = null;
        try {
            teamOwner = new TeamOwner(member2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Team team = new Team("Hapoel Beer Sheva", TeamStatus.Open, teamOwner, new Field("Isael", "Israel", "Beer Sheva", "6809815"));
        dbStub.addTeam(team);
        Member member3 = new Member("player", SHA1Function.hash("player"), "player@gmail.com", new Address("Israel", "Israel", "Haifa", "3189240"), "yossi");
        dbStub.addMember(member3);
        Player player3 = null;
        try {
            player3 = new Player(member3, new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        teamOwner.addPlayer(player3, PlayerRole.CAM);
        try {
            dbStub.addSeasonInLeague(season.getYear(), league.getLeagueName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SeasonInLeague seasonInLeague = null;
        try {
            seasonInLeague = dbStub.findSeasonInLeague(season.getYear(), league.getLeagueName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.game = new Game(team, team, seasonInLeague, LocalDateTime.now(), team.getField());
        game.setGameID(1);
        dbStub.addGame(game);
        game.addSideReferee(referee);
    }

    @Test
    public void testCreateGameEvent(){

        try {
            this.referee.createGameEvent(40, "new game event" , EventType.Foul, 1,  game.getHost().getTeamName(), game.getGuest().getTeamName(), this.game.getHost().getPlayers().get(0).getPlayer().getMember().getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, this.game.getEvents().size());
        assertEquals("new game event", this.game.getEvents().get(0).getDescription());
        assertEquals(40, this.game.getEvents().get(0).getGameMinute());
        this.referee.getMain().add(this.game);
        this.game.setMainReferee(this.referee);
        this.referee.createGameEvent(70, "new" , EventType.Foul, new Date(), this.game, this.game.getHost().getPlayers().get(0).getPlayer());
        assertEquals("new", this.game.getEvents().get(1).getDescription());
        assertEquals(70, this.game.getEvents().get(1).getGameMinute());
        assertEquals(2, this.game.getEvents().size());
        // test create game event with unauthorized referee
        Member member4 = new Member("cheater", SHA1Function.hash("cheater"), "cheater@gmail.com", new Address("Israel", "Israel", "Haifa", "6127824"), "shimon");
        dbStub.addMember(member4);
        try {
            Referee referee1 = new RefereeStub(member4);
            referee1.createGameEvent(10, "new event" , EventType.Foul, 1,  game.getHost().getTeamName(), game.getGuest().getTeamName(), this.game.getHost().getPlayers().get(0).getPlayer().getMember().getUserName());
        } catch (Exception e) {
            System.out.println("unauthorized referee was blocked from doing the change");
            assertEquals(2, this.game.getEvents().size());
        }
    }

    @Test
    public void testCreateGameReport(){
        try {
            //check with good referee
            String report = "hey just creating a report";
            this.referee.createReport(1, report);
            assertEquals(report, game.getReport());
            // test create game event with unauthorized referee
            Member member4 = new Member("cheater", SHA1Function.hash("cheater"), "cheater@gmail.com", new Address("Israel", "Israel", "Haifa", "6127824"), "shimon");
            dbStub.addMember(member4);
            Referee referee1 = new RefereeStub(member4);
            report = "bad report";
            referee1.createReport(1, report);
        } catch (Exception e) {
            System.out.println("unauthorized referee was blocked from doing the change");
            assertNotEquals("bad report", game.getReport());
        }
    }



    //    @Test
//    public void testEditGameEvent(){
//        Event gameEvent = this.game.getEvents().get(0);
//        Event newGameEvent = new Event(1, "new", EventType.Foul, this.game, this.game.getHost().getPlayers().get(0).getPlayer());
//        this.referee.editGameEvent(this.game, gameEvent, newGameEvent);
//        assertFalse(this.game.getEvents().contains(newGameEvent));
//        this.referee.getMain().add(this.game);
//        this.referee.editGameEvent(this.game, gameEvent, newGameEvent);
//        assertTrue(this.game.getEvents().contains(newGameEvent));
//    }
}
