package unit;


import com.domain.logic.AssociationSystem;
import com.domain.logic.SearchSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.Field;
import com.domain.logic.roles.Coach;
import com.domain.logic.roles.Player;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import com.domain.logic.utils.SHA1Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SearchSystemTest {

    private SearchSystem searchSystem;

    @BeforeEach
    public void beforeTestMethod(){
        AssociationSystem.getInstance().clearSystem();
        this.searchSystem = SearchSystem.getInstance();
        // coach & Team Owner
        Member member1 = new Member("coach", SHA1Function.hash("coach"), "coach@gmail.com", new Address("Israel", "Israel", "Tel Aviv", "6804035"), "yossi");
        try {
            Coach coach = new Coach(member1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // player
        Member member2 = new Member("player1", SHA1Function.hash("player1"), "player1@gmail.com", new Address("Israel", "Israel", "Haifa", "7906823"), "yoSSI");
        try {
            Player player2 = new Player( member2, new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //player
        Member member3 = new Member("player2", SHA1Function.hash("player2"), "player2@gmail.com", new Address("Israel", "Israel", "Beer Sheva", "7125820"), "shimon");
        try {
            Player player3 = new Player( member3, new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // team
        try {
            TeamOwner teamOwner = new TeamOwner(member1, "Hapoel Beer Sheva", TeamStatus.Open, new Field("Israel", "Israel", "Beer Sheva", "6808157"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    void search() {
        String searchResult;
        String[] check;
        // testing "search by name"
        searchResult = this.searchSystem.search(1, "Yossi");
        check = searchResult.split(",");
        assertEquals(2, check.length);

        searchResult = this.searchSystem.search(1, "Blue");
        assertEquals("[]", searchResult);

        // testing "search by category"

        // search coaches
        searchResult = this.searchSystem.search(2, "Coach");
        assertTrue(!searchResult.equals("[]"));
        assertEquals(1, searchResult.split(",").length);

        // search players
        searchResult = this.searchSystem.search(2, "Player");
        assertTrue(!searchResult.equals("[]"));
        assertEquals(2, searchResult.split(",").length);

        // search teams
        searchResult = this.searchSystem.search(2, "Team");
        assertTrue(!searchResult.equals("[]"));
        assertEquals(1, searchResult.split(",").length);

        // testing "search by keywords"
        searchResult = this.searchSystem.search(3, "2012");
        assertTrue(searchResult.equals("[]"));

        searchResult = this.searchSystem.search(3, "shimon");
        assertTrue(!searchResult.equals("[]"));
        assertEquals(1, searchResult.split(",").length);
    }
}