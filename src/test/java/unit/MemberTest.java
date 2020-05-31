package unit;


import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.data_types.Complaint;
import com.domain.logic.managers.ManageMembers;
import com.domain.logic.roles.Coach;
import com.domain.logic.roles.IRole;
import com.domain.logic.roles.Referee;
import com.domain.logic.users.Member;
import com.domain.logic.users.SystemManagerMember;
import com.domain.logic.utils.SHA1Function;
import com.stubs.ComplaintStub;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {
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
        member.writeComplaint("menu is broken2");
        assertTrue(systemManagerMember.readUnhandledComplaints().contains("menu is broken2"));
    }

    @Test
    void readResolvedComplaint() {
        String complaintData = "menu is broken";
        ComplaintStub complaint = new ComplaintStub(member, complaintData);
        member.getResolvedComplaints().add(complaint);
        complaint.setCompAns("some Ans");
        String funcAns = member.readResolvedComplaint(complaint.getCompID());
        assertEquals(complaint.getCompAns(), funcAns);
    }

    @Test
    void testGetSpecificRole() {
        try {
            Referee referee = new Referee(member);

            assertEquals(referee, member.getSpecificRole(Referee.class));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void notifyCompAns() {
        //checks if return good string to the member
        Complaint complaint = new Complaint(member, "something bad");
        member.getUnresolvedComplaints().add(complaint);
        String ans = member.notifyCompAns(complaint);
        assertEquals(ans, "Complaint " + complaint.getCompID() + " has been answered by System Manager");
        //checks the rightness of the data structure
        assertTrue(member.getResolvedComplaints().contains(complaint));
        assertFalse(member.getUnresolvedComplaints().contains(complaint));
    }

//    @Test
//    void setUserName() {
//        //checks scenario with no lapping data with another member
//        String newUserName = "bla";
//        assertTrue(member.setUserName(newUserName));
//        //checks scenario with lapping data with another member
//        newUserName = "pam";
//        Member memberTemp = new Member(newUserName, member.getPasswordHash(), "ssEmail",
//                new Address("", "", "", ""), "sdc sdf");
//        assertFalse(member.setUserName(newUserName));
//        ManageMembers.getInstance().removeMember(memberTemp.getUserName(), memberTemp.getPasswordHash());
//    }
//
//    @Test
//    void setPasswordHash() {
//        //checks scenario with no lapping data with another member
//        String newPass = "bla";
//        assertTrue(member.setPasswordHash(newPass));
//        //checks scenario with lapping data with another member
//        newPass = "pam";
//        Member memberTemp = new Member(member.getUserName(), SHA1Function.hash(newPass), "ssEmail",
//                new Address("", "", "", ""), "sdc sdf");
//        assertFalse(member.setPasswordHash(newPass));
//    }
}