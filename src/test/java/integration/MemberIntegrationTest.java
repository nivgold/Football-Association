package integration;

import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.users.Member;
import com.domain.logic.users.SystemManagerMember;
import com.domain.logic.utils.SHA1Function;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MemberIntegrationTest {
    private static Member member;
    private static SystemManagerMember systemManagerMember;

    @BeforeAll
    public static void initiate(){
        AssociationSystem.getInstance().clearSystem();
        member = new Member("korenISH", SHA1Function.hash("pass"), "korenEmail",
                new Address("", "", "", ""), "Koren Ishlach");
        systemManagerMember = new SystemManagerMember("sysMen", "admin", "sysEmail",
                new Address("a", "b", "c", "d"), "mr.Manager");
    }

    @Test
    void writeComplaint() {
        member.writeComplaint("menu is broken");
        assertTrue(systemManagerMember.readUnhandledComplaints().contains("menu is broken"));
        assertTrue(systemManagerMember.readUnhandledComplaints().size() == 1);
        member.writeComplaint("menu is broken2");
        assertTrue(systemManagerMember.readUnhandledComplaints().contains("menu is broken2"));
        assertTrue(systemManagerMember.readUnhandledComplaints().size() == 2);
        assertTrue(systemManagerMember.readUnhandledComplaints().get(1).contains("menu is broken2"));
        SystemManagerMember.removeComplaint(0);
        SystemManagerMember.removeComplaint(1);
    }

    @Test
    void readResolvedComplaint() {
        String complaintData = "menu is broken";
        member.writeComplaint(complaintData);
        int[] ansId = {0};
        String[] ans = {"Whatever Man"};
        systemManagerMember.answerComplaints(ansId, ans);
        String funcAns = member.readResolvedComplaint(0);
        assertTrue(funcAns != null);
        assertEquals(funcAns, "Whatever Man");
    }
}
