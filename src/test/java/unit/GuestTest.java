package unit;


import com.data.Dao;
import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.managers.ManageMembers;
import com.domain.logic.users.Guest;
import com.domain.logic.users.Member;
import com.domain.logic.utils.SHA1Function;
import com.stubs.DBStub;
import com.stubs.GuestStub;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuestTest {
    private static Guest guest;
    private static Member member;
    private static ManageMembers manageMembers = ManageMembers.getInstance();
    @BeforeAll
    public static void initiate() {
        AssociationSystem.getInstance().clearSystem();
        guest = new GuestStub("Koren", "Ishlach");
        member = new Member("korenISH", SHA1Function.hash("pass"), "korenEmail",
                new Address("", "", "", ""), "Koren Ishlach");
        DBStub.members.add(member);
    }

    @Test
    void login() throws Exception {
        //fail to log in
        try {
            assertEquals(null, guest.login("bim", "bam"));
        } catch (Exception e) {

        }

        //log in to existing member
        assertEquals(member, guest.login("korenISH", "pass"));
    }

//    @Test
//    void registerAsMember() {
//        //fail to register cause of an existing member
//        assertFalse(null != guest.registerAsMember(member.getUserName(), "pass", "email", "d", "", "", ""));
//
//        //register
//        assertTrue(null != guest.registerAsMember("newMem", "pass", "email", "d", "", "", ""));
//        manageMembers.removeMember("newMem", SHA1Function.hash("pass"));
//    }
}