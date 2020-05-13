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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


class RefereeIntegration {

    private Game game;
    private Referee referee;
    private AssociationSystem system;

    @BeforeEach
    public void beforeTestMethod(){
        AssociationSystem.getInstance().clearSystem();
        Member member = new Member("referee", SHA1Function.hash("referee"), "referee@gmail.com", new Address("Israel", "Israel", "Haifa", "6127824"), "shimon");
        try {
            this.referee = new Referee(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.system = AssociationSystem.getInstance();

        League league = new League("league1");
        Season season = new Season(2012);
        league.addSeason(season);
        Member member2 = new Member("owner", SHA1Function.hash("owner"), "owner@gmail.com", new Address("Israel", "Israel", "Haifa", "3189240"), "moshe");
        TeamOwner teamOwner = null;
        try {
            teamOwner = new TeamOwner(member2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Team team = new Team("Hapoel Beer Sheva", TeamStatus.Open, teamOwner, new Field("Isael", "Israel", "Beer Sheva", "6809815"));
        Member member3 = new Member("player", SHA1Function.hash("player"), "player@gmail.com", new Address("Israel", "Israel", "Haifa", "3189240"), "yossi");
        Player player3 = null;
        try {
            player3 = new Player(member3, new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        teamOwner.addPlayer(player3, PlayerRole.CAM);
        SeasonInLeague seasonInLeague = new SeasonInLeague(league, season);
        this.game = new Game(team, team, seasonInLeague, LocalDateTime.now(), team.getField());
        Event event = new Event(1, "a", EventType.Foul, this.game, player3);
        this.game.getEvents().add(event);
    }

    @Test
    public void testCreateGameEvent(){
        this.referee.createGameEvent(40, "new" , EventType.Foul, new Date(), this.game, this.game.getHost().getPlayers().get(0).getPlayer());
        assertEquals(1, this.game.getEvents().size());
        this.referee.getMain().add(this.game);
        this.game.setMainReferee(this.referee);
        this.referee.createGameEvent(40, "new" , EventType.Foul, new Date(), this.game, this.game.getHost().getPlayers().get(0).getPlayer());
        assertEquals(2, this.game.getEvents().size());
    }

    @Test
    public void testEditGameEvent(){
        Event gameEvent = this.game.getEvents().get(0);
        Event newGameEvent = new Event(1, "new", EventType.Foul, this.game, this.game.getHost().getPlayers().get(0).getPlayer());
        this.referee.editGameEvent(this.game, gameEvent, newGameEvent);
        assertFalse(this.game.getEvents().contains(newGameEvent));
        this.referee.getMain().add(this.game);
        this.referee.editGameEvent(this.game, gameEvent, newGameEvent);
        assertTrue(this.game.getEvents().contains(newGameEvent));
    }
}
