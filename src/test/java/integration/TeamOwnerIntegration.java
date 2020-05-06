package integration;

import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.enums.PlayerRole;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.Field;
import com.domain.logic.football.Team;
import com.domain.logic.roles.Coach;
import com.domain.logic.roles.Player;
import com.domain.logic.roles.TeamManager;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import com.domain.logic.utils.SHA1Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


class TeamOwnerIntegration {

    private TeamOwner teamOwner;
    private AssociationSystem system;

    @BeforeEach
    public void beforeTestMethod(){
        AssociationSystem.getInstance().clearSystem();
        Member member = new Member("owner", SHA1Function.hash("owner"), "owner@gmail.com", new Address("Israel", "Israel", "Haifa", "3189240"), "moshe");
        this.teamOwner = new TeamOwner(member);
        Team team = new Team("Hapoel Beer Sheva", TeamStatus.Open, this.teamOwner, new Field("Isael", "Israel", "Beer Sheva", "6809815"));
        this.system = AssociationSystem.getInstance();
    }

    @Test
    public void testAppointTeamOwner(){
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");

        // closed Team
        this.teamOwner.getTeam().setStatus(TeamStatus.Closed);
        this.teamOwner.appointTeamOwner(member);
        assertEquals(1, this.teamOwner.getTeam().getTeam_owners().size());
        // adding successfully
        this.teamOwner.getTeam().setStatus(TeamStatus.Open);
        this.teamOwner.appointTeamOwner(member);
        assertTrue(this.teamOwner.getTeam().getTeam_owners().get(1).getMember().equals(member));

        // already Team Owner in the team
        this.teamOwner.appointTeamOwner(member);
        assertEquals(2, this.teamOwner.getTeam().getTeam_owners().size());
    }

    @Test
    public void testRemoveTeamOwner(){
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
        this.teamOwner.appointTeamOwner(member);
        TeamOwner shimonTeamOwner = this.teamOwner.getTeam().getTeam_owners().get(1);

        // not authorized Team owner
        shimonTeamOwner.removeTeamOwner(this.teamOwner);
        assertTrue(this.teamOwner.getTeam().getTeam_owners().contains(this.teamOwner));

        // closed Team
        this.teamOwner.getTeam().setStatus(TeamStatus.Closed);
        this.teamOwner.removeTeamOwner(shimonTeamOwner);
        assertTrue(this.teamOwner.getTeam().getTeam_owners().contains(shimonTeamOwner));

        // successful removal
        this.teamOwner.getTeam().setStatus(TeamStatus.Open);
        this.teamOwner.removeTeamOwner(shimonTeamOwner);
        assertEquals(1, this.teamOwner.getTeam().getTeam_owners().size());
    }

    @Test
    public void testAppointTeamManager(){
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");

        // closed Team
        this.teamOwner.getTeam().setStatus(TeamStatus.Closed);
        this.teamOwner.appointTeamManager(member);
        assertEquals(0, this.teamOwner.getTeam().getTeam_managers().size());

        // adding successfully
        this.teamOwner.getTeam().setStatus(TeamStatus.Open);
        this.teamOwner.appointTeamManager(member);
        assertTrue(this.teamOwner.getTeam().getTeam_managers().get(0).getMember().equals(member));

        // already team manager
        this.teamOwner.appointTeamManager(member);
        assertEquals(1, this.teamOwner.getTeam().getTeam_managers().size());
    }

    @Test
    public void testRemoveTeamManager(){
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
        Member member2 = new Member("manager", SHA1Function.hash("manager"), "manager@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "5815719"), "yossi");
        this.teamOwner.appointTeamManager(member2);
        this.teamOwner.appointTeamOwner(member);
        TeamOwner shimonTeamOwner = this.teamOwner.getTeam().getTeam_owners().get(1);
        TeamManager teamManager = this.teamOwner.getTeam().getTeam_managers().get(0);

        // not authorized removal of team manager
        shimonTeamOwner.removeTeamManager(teamManager);
        assertEquals(1, this.teamOwner.getTeam().getTeam_managers().size());

        // closed Team
        this.teamOwner.getTeam().setStatus(TeamStatus.Closed);
        this.teamOwner.removeTeamManager(teamManager);
        assertEquals(1, this.teamOwner.getTeam().getTeam_managers().size());
        // successful removal
        this.teamOwner.getTeam().setStatus(TeamStatus.Open);
        this.teamOwner.removeTeamManager(teamManager);
        assertEquals(0, this.teamOwner.getTeam().getTeam_managers().size());
    }

    @Test
    public void testAppointCoach(){
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
        Coach coach = new Coach(member);

        // closed Team
        this.teamOwner.getTeam().setStatus(TeamStatus.Closed);
        this.teamOwner.appointCoach(coach);
        assertEquals(0, this.teamOwner.getTeam().getCoaches().size());

        // adding successfully
        this.teamOwner.getTeam().setStatus(TeamStatus.Open);
        this.teamOwner.appointCoach(coach);
        assertEquals(coach, this.teamOwner.getTeam().getCoaches().get(0).getCoach());
    }

    @Test
    public void testRemoveCoach(){
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
        Coach coach = new Coach(member);
        this.teamOwner.appointCoach(coach);

        // closed Team
        this.teamOwner.getTeam().setStatus(TeamStatus.Closed);
        this.teamOwner.removeCoach(coach);
        assertEquals(1, this.teamOwner.getTeam().getCoaches().size());

        // successful removal of coach
        this.teamOwner.getTeam().setStatus(TeamStatus.Open);
        this.teamOwner.removeCoach(coach);
        assertEquals(0,this.teamOwner.getTeam().getCoaches().size());
    }

    @Test
    public void testAddPlayer(){
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
        Player player = new Player(member, new Date());

        // closed Team
        this.teamOwner.getTeam().setStatus(TeamStatus.Closed);
        this.teamOwner.addPlayer(player, PlayerRole.CM);
        assertEquals(0, this.teamOwner.getTeam().getPlayers().size());

        // adding successfully
        this.teamOwner.getTeam().setStatus(TeamStatus.Open);
        this.teamOwner.addPlayer(player, PlayerRole.CM);
        assertEquals(player, this.teamOwner.getTeam().getPlayers().get(0).getPlayer());

        // already exist
        this.teamOwner.addPlayer(player, PlayerRole.CAM);
        assertEquals(1, this.teamOwner.getTeam().getPlayers().size());
    }

    @Test
    public void testRemovePlayer(){
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
        Player player = new Player( member, new Date());

        // the player is not on the Team
        this.teamOwner.removePlayer(player);
        assertEquals(0, this.teamOwner.getTeam().getPlayers().size());



        this.teamOwner.addPlayer(player, PlayerRole.CM);

        // closed Team
        this.teamOwner.getTeam().setStatus(TeamStatus.Closed);
        this.teamOwner.removePlayer(player);
        assertEquals(1, this.teamOwner.getTeam().getPlayers().size());
        assertEquals(player, this.teamOwner.getTeam().getPlayers().get(0).getPlayer());

        // successful removal
        this.teamOwner.getTeam().setStatus(TeamStatus.Open);
        this.teamOwner.removePlayer(player);
        assertEquals(0, this.teamOwner.getTeam().getPlayers().size());
    }

    @Test
    public void testRemoveYourself(){
        //closed Team
        this.teamOwner.getTeam().setStatus(TeamStatus.Closed);
        assertFalse(this.teamOwner.removeYourself());
        assertEquals(1, this.teamOwner.getTeam().getTeam_owners().size());
        assertEquals(this.teamOwner, teamOwner.getTeam().getTeam_owners().get(0));


        this.teamOwner.getTeam().setStatus(TeamStatus.Open);
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
        this.teamOwner.appointTeamOwner(member);
        TeamOwner shimonTeamOwner = this.teamOwner.getTeam().getTeam_owners().get(1);

        assertEquals(2, this.teamOwner.getTeam().getTeam_owners().size());

        assertTrue(shimonTeamOwner.removeYourself());
        assertEquals(1, this.teamOwner.getTeam().getTeam_owners().size());
        assertEquals(null, shimonTeamOwner.getTeam());
    }
}
