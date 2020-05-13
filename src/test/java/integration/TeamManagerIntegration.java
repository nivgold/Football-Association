package integration;


import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.Field;
import com.domain.logic.football.Team;
import com.domain.logic.roles.IRole;
import com.domain.logic.roles.TeamManager;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import com.domain.logic.utils.SHA1Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class TeamManagerIntegration {

    private TeamManager teamManager;
    private AssociationSystem system;

    @BeforeEach
    public void beforeTestMethod(){
        AssociationSystem.getInstance().clearSystem();
        Member memberOwner = new Member("owner", SHA1Function.hash("owner"), "owner@gmail.com", new Address("Israel", "Israel", "Haifa", "3189240"), "moshe");
        Member memberManager = new Member("manager", SHA1Function.hash("manager"), "manager@gmail.com", new Address("Israel", "Israel", "Haifa", "3189240"), "moshe");
        TeamOwner teamOwner = null;
        try {
            teamOwner = new TeamOwner(memberOwner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Team team = new Team("Hapoel Beer Sheva", TeamStatus.Open, teamOwner, new Field("Isael", "Israel", "Beer Sheva", "6809815"));
        try {
            teamOwner.appointTeamManager(memberManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.teamManager = team.getTeam_managers().get(0);
    }

    @Test
    public void testRemoveYourself(){
        Team team = this.teamManager.getTeam();
        TeamOwner teamOwner = this.teamManager.getAppointer();
        assertNotNull(team);
        assertTrue(team.getTeam_managers().contains(this.teamManager));
        try {
            this.teamManager.removeYourself();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, team.getTeam_managers().size());
        assertNull(this.teamManager.getTeam());
        for (IRole role : this.teamManager.getMember().getRoles()){
            assertFalse(role instanceof TeamManager);
        }
    }
}
