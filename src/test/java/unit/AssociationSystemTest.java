package unit;
import com.domain.logic.AssociationSystem;
import com.domain.logic.football.Team;
import com.domain.logic.managers.ManageMembers;
import com.domain.logic.managers.ManageTeams;
import com.domain.logic.utils.SHA1Function;
import com.stubs.DBStub;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AssociationSystemTest {

    @Test
    void resetSystem() {
        AssociationSystem.getInstance().clearSystem();
        try {
            assertTrue(AssociationSystem.getInstance().resetSystem());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(7, DBStub.members.size());
    }
}