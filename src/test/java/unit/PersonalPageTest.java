package unit;
import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.PersonalPage;
import com.domain.logic.roles.Coach;
import com.domain.logic.users.Member;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;



public class PersonalPageTest {

    Member member = new Member("avi","1234","avi@avi.avi",null,"avi avi");
    Coach coach = new Coach(member);
    PersonalPage personalPage = new PersonalPage(coach);


    @Test
    void testRegister() {
        AssociationSystem.getInstance().clearSystem();
        personalPage.register(member);
        assertTrue(personalPage.getPersonalPageObservers().contains(member));
    }

    @Test
    void testRemove() {
        AssociationSystem.getInstance().clearSystem();
        personalPage.register(member);
        personalPage.remove(member);
        assertFalse(personalPage.getPersonalPageObservers().contains(member));
    }
}
