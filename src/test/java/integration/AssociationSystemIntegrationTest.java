package integration;

import com.domain.logic.AssociationSystem;
import com.domain.logic.football.Team;
import com.domain.logic.managers.ManageMembers;
import com.domain.logic.managers.ManageTeams;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.utils.SHA1Function;
import com.stubs.DBStub;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AssociationSystemIntegrationTest {
    DBStub dbStub = DBStub.getInstance();
    @Test
    void resetSystem() throws Exception {
        AssociationSystem.getInstance().clearSystem();
        try {
            assertTrue(AssociationSystem.getInstance().resetSystem());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(dbStub.findMember("managerSys", SHA1Function.hash("admin")) != null);
        assertTrue(dbStub.findAllReferees().size() == 3);
        assertTrue(dbStub.findAllTeamOwner().size() == 2);
        assertTrue(dbStub.findAllAssociationAgent().size() == 1);
        ArrayList<Team> allTeams = dbStub.getAllTeams();
        assertTrue(allTeams.size() == 2);
        for (Team team : allTeams) {
            TeamOwner teamOwner = team.getTeam_owners().get(0);
            assertTrue(teamOwner != null);
            assertEquals(teamOwner.getTeam(), team);
            assertTrue(team.getField() != null);
            assertEquals(team, team.getField().getOwnerTeam());
            assertTrue(team.getField().getLocation() != null);
        }
    }
}
