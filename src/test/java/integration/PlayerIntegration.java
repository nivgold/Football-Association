package integration;

import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.Field;
import com.domain.logic.football.Team;
import com.domain.logic.roles.Player;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import com.domain.logic.utils.SHA1Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

class PlayerIntegration {

    private Player player;

    @BeforeEach
    public void beforeTestMethod(){
        AssociationSystem.getInstance().clearSystem();
        Address add = new Address("Israel", "Israel", "Haifa,", "1928654");
        Member member = new Member("owner", SHA1Function.hash("owner"), "owner@gmail.com", add, "moshe");
        Member member2 = new Member("player", SHA1Function.hash("player"), "player@gmail.com", add, "shimon");
        TeamOwner teamOwner = null;
        try {
            teamOwner = new TeamOwner(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.player = new Player(member2, new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Field field = new Field("Israel", "hasharon", "Herzliya", "12345");
        Team team = new Team("Hapoel Haifa", TeamStatus.Open, teamOwner, field);
    }

    @Test
    public void testRemoveYourself(){
        // TODO
    }

    @Test
    public void testRemoveFromTeam(){
        // TODO
    }
}
