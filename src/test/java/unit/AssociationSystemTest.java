package unit;
import com.domain.logic.AssociationSystem;
import com.domain.domaincontroller.managers.ManageMembers;
import com.domain.domaincontroller.managers.ManageTeams;
import com.domain.logic.football.Team;
import com.domain.logic.utils.SHA1Function;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AssociationSystemTest {

    @Test
    void resetSystem() {
        AssociationSystem.getInstance().clearSystem();
        assertTrue(AssociationSystem.getInstance().resetSystem());
        assertTrue(ManageMembers.getInstance().findMember("managerSys", SHA1Function.hash("admin")) != null);
        assertTrue(ManageMembers.getInstance().findAllReferees().size() == 3);
        assertTrue(ManageMembers.getInstance().findAllTeamOwner().size() == 2);
        assertTrue(ManageMembers.getInstance().findAllAssociationAgent().size() == 1);
        ArrayList<Team> allTeams = ManageTeams.getInstance().getAllTeams();
        assertTrue(allTeams.size() == 2);
    }
}