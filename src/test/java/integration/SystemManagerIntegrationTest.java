package integration;

import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.data_types.Complaint;
import com.domain.logic.users.Member;
import com.domain.logic.users.SystemManagerMember;
import com.domain.logic.utils.SHA1Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class SystemManagerIntegrationTest {
    private Member member;
    private SystemManagerMember systemManagerMember;

    @BeforeEach
    void setUp(){
        AssociationSystem.getInstance().clearSystem();
        this.member = new Member("korenISH", SHA1Function.hash("pass"), "korenEmail",
                new Address("", "", "", ""), "Koren Ishlach");
        this.systemManagerMember = new SystemManagerMember("sysMen", "admin", "sysEmail",
                new Address("a", "b", "c", "d"), "mr.Manager");
    }

    @Test
    void answerComplaints() {
        //TODO create one that's from unit:
        Complaint complaint = new Complaint(member, "bim bam bom");
        SystemManagerMember.addComplaint(complaint);
        int [] compIds = new int[1];
        compIds[0] = complaint.getCompID();
        String[] compAns = new String[1];
        compAns[0] = "whatever man";
        assertTrue(this.systemManagerMember.answerComplaints(compIds, compAns));
        assertEquals(complaint.getCompAns(), compAns[0]);
    }
}
