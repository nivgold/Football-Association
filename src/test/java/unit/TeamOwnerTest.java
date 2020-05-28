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
import com.stubs.DBStub;
import com.stubs.TeamOwnerStub;
import com.stubs.TeamStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TeamOwnerTest {

    private TeamOwner teamOwner;
    private TeamStub teamStub;

    @BeforeEach
    public void beforeTestMethod() throws Exception {
        AssociationSystem.getInstance().clearSystem();
        Member member = new Member("owner", SHA1Function.hash("owner"), "owner@gmail.com", new Address("Israel", "Israel", "Haifa", "3189240"), "moshe");
        this.teamOwner = new TeamOwnerStub(member);
        // creating a team for the team owner
        this.teamStub = new TeamStub("Hapoel Beer Sheva", TeamStatus.Open, this.teamOwner, new Field("Israel", "Israel", "Haifa", "2018756"));
        this.teamStub.teamOwners.add(this.teamOwner);
        this.teamOwner.setTeam(this.teamStub);
        DBStub.members.add(member);
        DBStub.teams.add(teamStub);
    }

    @Test
    public void createTeam() {
        // failed to create team because already has
        try {
            this.teamOwner.createTeam("Blabla", new Field("123", "123", "123","123"));
        } catch (Exception e) {
        }
        assertEquals(1, DBStub.teams.size());

        Member member = new Member("owner2", SHA1Function.hash("123"), "123", new Address("123", "123", "!23", "123"), "name");
        TeamOwner teamOwner2 = null;
        try {
            teamOwner2 = new TeamOwnerStub(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        DBStub.members.add(member);
        // team already exists
        try {
            teamOwner2.createTeam("Hapoel Beer Sheva", new Field("Israel", "Israel", "Haifa", "2018756"));
        } catch (Exception e) {
        }
        assertEquals(1, DBStub.teams.size());

        try {
            teamOwner2.createTeam("Blaa", new Field("Israel", "Israel", "Haifa", "2018756"));
        } catch (Exception e) {
        }
        assertEquals(2, DBStub.teams.size());

    }
/*
    @Test
    public void appointTeamOwner() {
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");

        // closed Team
        this.teamStub.teamStatus = TeamStatus.Closed;
        try {
            this.teamOwner.appointTeamOwner(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, this.teamStub.getTeam_owners().size());
        // adding successfully
        this.teamStub.teamStatus = TeamStatus.Open;
        try {
            this.teamOwner.appointTeamOwner(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(this.teamStub.getTeam_owners().get(1).getMember().equals(member));

        // already Team Owner in the team
        try {
            this.teamOwner.appointTeamOwner(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(2, this.teamStub.getTeam_owners().size());

    }

    @Test
    public void removeTeamOwner() {
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
        try {
            this.teamOwner.appointTeamOwner(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TeamOwner shimonTeamOwner = this.teamStub.getTeam_owners().get(1);

        // not authorized Team owner
        try {
            shimonTeamOwner.removeTeamOwner(this.teamOwner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(this.teamStub.getTeam_owners().contains(this.teamOwner));

        // closed Team
        this.teamStub.teamStatus = TeamStatus.Closed;
        try {
            this.teamOwner.removeTeamOwner(shimonTeamOwner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(this.teamStub.getTeam_owners().contains(shimonTeamOwner));

        // successful removal
        this.teamStub.teamStatus = TeamStatus.Open;
        try {
            this.teamOwner.removeTeamOwner(shimonTeamOwner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, this.teamStub.getTeam_owners().size());

    }

    @Test
    public void appointTeamManager() {
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");

        // closed Team
        this.teamStub.teamStatus = TeamStatus.Closed;
        try {
            this.teamOwner.appointTeamManager(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(0, this.teamStub.getTeam_managers().size());

        // adding successfully
        this.teamStub.teamStatus = TeamStatus.Open;
        try {
            this.teamOwner.appointTeamManager(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(this.teamStub.getTeam_managers().get(0).getMember().equals(member));

        // already team manager
        try {
            this.teamOwner.appointTeamManager(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, this.teamStub.getTeam_managers().size());
    }

    @Test
    public void removeTeamManager() {
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
        Member member2 = new Member("manager", SHA1Function.hash("manager"), "manager@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "5815719"), "yossi");
        try {
            this.teamOwner.appointTeamManager(member2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.teamOwner.appointTeamOwner(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TeamOwner shimonTeamOwner = this.teamStub.getTeam_owners().get(1);
        TeamManager teamManager = this.teamStub.getTeam_managers().get(0);

        // not authorized removal of team manager
        try {
            shimonTeamOwner.removeTeamManager(teamManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, this.teamStub.getTeam_managers().size());

        // closed Team
        this.teamStub.teamStatus = TeamStatus.Closed;
        try {
            this.teamOwner.removeTeamManager(teamManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, this.teamStub.getTeam_managers().size());
        // successful removal
        this.teamStub.teamStatus = TeamStatus.Open;
        try {
            this.teamOwner.removeTeamManager(teamManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(0, this.teamStub.getTeam_managers().size());
    }

    @Test
    public void appointCoach() {
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
        Coach coach = null;
        try {
            coach = new Coach(member);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        Coach coach = null;
        try {
            coach = new Coach(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.teamOwner.appointCoach(coach);

        // closed Team
        this.teamStub.teamStatus = TeamStatus.Closed;
        try {
            this.teamOwner.removeCoach(coach);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, this.teamStub.getCoaches().size());

        // successful removal of coach
        this.teamStub.teamStatus = TeamStatus.Open;
        try {
            this.teamOwner.removeCoach(coach);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(0,this.teamStub.getCoaches().size());
    }

    @Test
    public void addPlayer() {
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
        Player player = null;
        try {
            player = new Player( member, new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        Player player = null;
        try {
            player = new Player(member, new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // the player is not on the Team
        try {
            this.teamOwner.removePlayer(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(0, this.teamStub.getPlayers().size());



        this.teamOwner.addPlayer(player, PlayerRole.CM);

        // closed Team
        this.teamStub.teamStatus = TeamStatus.Closed;
        try {
            this.teamOwner.removePlayer(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, this.teamStub.getPlayers().size());
        assertEquals(player, this.teamStub.getPlayers().get(0).getPlayer());

        // successful removal
        this.teamStub.teamStatus = TeamStatus.Open;
        try {
            this.teamOwner.removePlayer(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(0, this.teamStub.getPlayers().size());
    }


    @Test
    public void removeYourself() {
        //closed Team
        this.teamStub.teamStatus = TeamStatus.Closed;
        try {
            assertFalse(this.teamOwner.removeYourself());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, this.teamStub.getTeam_owners().size());
        assertEquals(this.teamOwner, teamStub.getTeam_owners().get(0));


        this.teamStub.teamStatus = TeamStatus.Open;
        Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
        try {
            this.teamOwner.appointTeamOwner(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TeamOwner shimonTeamOwner = this.teamStub.getTeam_owners().get(1);

        assertEquals(2, this.teamStub.getTeam_owners().size());

        try {
            assertTrue(shimonTeamOwner.removeYourself());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, this.teamStub.getTeam_owners().size());
        assertEquals(null, shimonTeamOwner.getTeam());
    }

 */
}