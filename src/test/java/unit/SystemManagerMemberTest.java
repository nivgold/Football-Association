package unit;


import com.domain.logic.AssociationSystem;
import com.domain.domaincontroller.managers.ManageMembers;
import com.domain.domaincontroller.recommender_system_strategies.GamesHistoryRecommenderStrategy;
import com.domain.logic.data_types.Address;
import com.domain.logic.data_types.Complaint;
import com.domain.logic.users.Member;
import com.domain.logic.users.SystemManagerMember;
import com.domain.logic.utils.SHA1Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SystemManagerMemberTest {
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
        Complaint complaint = new Complaint(member, "bim bam bom");
        SystemManagerMember.addComplaint(complaint);
        int [] compIds = new int[1];
        compIds[0] = complaint.getCompID();
        String[] compAns = new String[1];
        compAns[0] = "whatever man";
        assertTrue(this.systemManagerMember.answerComplaints(compIds, compAns));
        assertEquals(complaint.getCompAns(), compAns[0]);
    }

    @Test
    void appointSystemManager() {
        //checks if he indeed became sys manager
        Member app = this.systemManagerMember.appointSystemManager(member);
        assertTrue(app instanceof SystemManagerMember);
        //checks if he was saved in the general data that way
        assertEquals(app, ManageMembers.getInstance().findMember(member.getUserName(), member.getPasswordHash()));
        //checks if fail
        assertTrue(this.systemManagerMember.appointSystemManager(app) == null);
    }

    @Test
    void activateRecommenderSystem() {
        assertTrue(this.systemManagerMember.activateRecommenderSystem(new GamesHistoryRecommenderStrategy()));
    }
}