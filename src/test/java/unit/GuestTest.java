package unit;


import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.managers.ManageMembers;
import com.domain.logic.users.Guest;
import com.domain.logic.users.Member;
import com.domain.logic.utils.SHA1Function;
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
        guest = new Guest("Koren", "Ishlach");
        member = new Member("korenISH", SHA1Function.hash("pass"), "korenEmail",
                new Address("", "", "", ""), "Koren Ishlach");
    }

    @Test
    void login() {
        //fail to log in
        try {
            assertEquals(null, guest.login("bim", "bam"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //log in to existing member
        assertEquals(manageMembers.findMember(member.getUserName(), member.getPasswordHash()), member);
        Member testor = null;
        try {
            testor = guest.login(member.getUserName(), "pass");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(testor, member);
    }

    @Test
    void registerAsMember() {
        //fail to register cause of an existing member
        assertFalse(null != guest.registerAsMember(member.getUserName(), "pass", "email", "d", "", "", ""));

        //register
        assertTrue(null != guest.registerAsMember("newMem", "pass", "email", "d", "", "", ""));
        manageMembers.removeMember("newMem", SHA1Function.hash("pass"));
    }

    @Test
    void search() {
        //checks that member was found
       assertEquals(guest.search(1, "Koren Ishlach"), "["+member.toString()+"]");
       assertEquals(guest.search(3, "Koren Ishlach"), "[Koren Ishlach]");
    }
}