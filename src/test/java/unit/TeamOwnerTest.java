package unit;


import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.enums.PlayerRole;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.Field;
import com.domain.logic.roles.Coach;
import com.domain.logic.roles.Player;
import com.domain.logic.roles.TeamManager;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import com.domain.logic.utils.SHA1Function;
import com.stubs.TeamStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TeamOwnerTest {

    private TeamOwner teamOwner;
    private TeamStub teamStub;

    @BeforeEach
    public void beforeTestMethod(){
        AssociationSystem.getInstance().clearSystem();
        Member member = new Member("owner", SHA1Function.hash("owner"), "owner@gmail.com", new Address("Israel", "Israel", "Haifa", "3189240"), "moshe");
        this.teamOwner = new TeamOwner(member);
        // creating a team for the team owner
        this.teamStub = new TeamStub("Hapoel Beer Sheva", TeamStatus.Open, this.teamOwner, new Field("Israel", "Israel", "Haifa", "2018756"));
        this.teamStub.teamOwners.add(this.teamOwner);
        this.teamOwner.setTeam(this.teamStub);
    }

    @Test
    public void appointTeamOwner() {
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");

        // closed Team
        this.teamStub.teamStatus = TeamStatus.Closed;
        this.teamOwner.appointTeamOwner(member);
        assertEquals(1, this.teamStub.getTeam_owners().size());
        // adding successfully
        this.teamStub.teamStatus = TeamStatus.Open;
        this.teamOwner.appointTeamOwner(member);
        assertTrue(this.teamStub.getTeam_owners().get(1).getMember().equals(member));

        // already Team Owner in the team
        this.teamOwner.appointTeamOwner(member);
        assertEquals(2, this.teamStub.getTeam_owners().size());

    }

    @Test
    public void removeTeamOwner() {
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
        this.teamOwner.appointTeamOwner(member);
        TeamOwner shimonTeamOwner = this.teamStub.getTeam_owners().get(1);

        // not authorized Team owner
        shimonTeamOwner.removeTeamOwner(this.teamOwner);
        assertTrue(this.teamStub.getTeam_owners().contains(this.teamOwner));

        // closed Team
        this.teamStub.teamStatus = TeamStatus.Closed;
        this.teamOwner.removeTeamOwner(shimonTeamOwner);
        assertTrue(this.teamStub.getTeam_owners().contains(shimonTeamOwner));

        // successful removal
        this.teamStub.teamStatus = TeamStatus.Open;
        this.teamOwner.removeTeamOwner(shimonTeamOwner);
        assertEquals(1, this.teamStub.getTeam_owners().size());

    }

    @Test
    public void appointTeamManager() {
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");

        // closed Team
        this.teamStub.teamStatus = TeamStatus.Closed;
        this.teamOwner.appointTeamManager(member);
        assertEquals(0, this.teamStub.getTeam_managers().size());

        // adding successfully
        this.teamStub.teamStatus = TeamStatus.Open;
        this.teamOwner.appointTeamManager(member);
        assertTrue(this.teamStub.getTeam_managers().get(0).getMember().equals(member));

        // already team manager
        this.teamOwner.appointTeamManager(member);
        assertEquals(1, this.teamStub.getTeam_managers().size());
    }

    @Test
    public void removeTeamManager() {
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
        Member member2 = new Member("manager", SHA1Function.hash("manager"), "manager@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "5815719"), "yossi");
        this.teamOwner.appointTeamManager(member2);
        this.teamOwner.appointTeamOwner(member);
        TeamOwner shimonTeamOwner = this.teamStub.getTeam_owners().get(1);
        TeamManager teamManager = this.teamStub.getTeam_managers().get(0);

        // not authorized removal of team manager
        shimonTeamOwner.removeTeamManager(teamManager);
        assertEquals(1, this.teamStub.getTeam_managers().size());

        // closed Team
        this.teamStub.teamStatus = TeamStatus.Closed;
        this.teamOwner.removeTeamManager(teamManager);
        assertEquals(1, this.teamStub.getTeam_managers().size());
        // successful removal
        this.teamStub.teamStatus = TeamStatus.Open;
        this.teamOwner.removeTeamManager(teamManager);
        assertEquals(0, this.teamStub.getTeam_managers().size());
    }

    @Test
    public void appointCoach() {
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
        Coach coach = new Coach(member);

        // closed Team
        this.teamStub.teamStatus = TeamStatus.Closed;
        this.teamOwner.appointCoach(coach);
        assertEquals(0, this.teamStub.getCoaches().size());

        // adding successfully
        this.teamStub.teamStatus = TeamStatus.Open;
        this.teamOwner.appointCoach(coach);
        assertEquals(coach, this.teamStub.getCoaches().get(0).getCoach());
    }

    @Test
    public void removeCoach() {
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
        Coach coach = new Coach(member);
        this.teamOwner.appointCoach(coach);

        // closed Team
        this.teamStub.teamStatus = TeamStatus.Closed;
        this.teamOwner.removeCoach(coach);
        assertEquals(1, this.teamStub.getCoaches().size());

        // successful removal of coach
        this.teamStub.teamStatus = TeamStatus.Open;
        this.teamOwner.removeCoach(coach);
        assertEquals(0,this.teamStub.getCoaches().size());
    }

    @Test
    public void addPlayer() {
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
        Player player = new Player( member, new Date());

        // closed Team
        this.teamStub.teamStatus = TeamStatus.Closed;
        this.teamOwner.addPlayer(player, PlayerRole.CM);
        assertEquals(0, this.teamStub.getPlayers().size());

        // adding successfully
        this.teamStub.teamStatus = TeamStatus.Open;
        this.teamOwner.addPlayer(player, PlayerRole.CM);
        assertEquals(player, this.teamStub.getPlayers().get(0).getPlayer());

        // already exist
        this.teamOwner.addPlayer(player, PlayerRole.CAM);
        assertEquals(1, this.teamStub.getPlayers().size());

    }

    @Test
    public void removePlayer() {
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
        Player player = new Player(member, new Date());

        // the player is not on the Team
        this.teamOwner.removePlayer(player);
        assertEquals(0, this.teamStub.getPlayers().size());



        this.teamOwner.addPlayer(player, PlayerRole.CM);

        // closed Team
        this.teamStub.teamStatus = TeamStatus.Closed;
        this.teamOwner.removePlayer(player);
        assertEquals(1, this.teamStub.getPlayers().size());
        assertEquals(player, this.teamStub.getPlayers().get(0).getPlayer());

        // successful removal
        this.teamStub.teamStatus = TeamStatus.Open;
        this.teamOwner.removePlayer(player);
        assertEquals(0, this.teamStub.getPlayers().size());
    }


    @Test
    public void removeYourself() {
        //closed Team
        this.teamStub.teamStatus = TeamStatus.Closed;
        assertFalse(this.teamOwner.removeYourself());
        assertEquals(1, this.teamStub.getTeam_owners().size());
        assertEquals(this.teamOwner, teamStub.getTeam_owners().get(0));


        this.teamStub.teamStatus = TeamStatus.Open;
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
        this.teamOwner.appointTeamOwner(member);
        TeamOwner shimonTeamOwner = this.teamStub.getTeam_owners().get(1);

        assertEquals(2, this.teamStub.getTeam_owners().size());

        assertTrue(shimonTeamOwner.removeYourself());
        assertEquals(1, this.teamStub.getTeam_owners().size());
        assertEquals(null, shimonTeamOwner.getTeam());
    }
}