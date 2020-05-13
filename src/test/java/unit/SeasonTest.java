package unit;


import com.domain.logic.AssociationSystem;
import com.domain.logic.football.League;
import com.domain.logic.football.Season;
import com.domain.logic.football.SeasonInLeague;
import com.domain.logic.policies.Policy;
import com.domain.logic.users.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeasonTest {

    League league = new League("test league");
    Member member1 = new Member("Talfrim","123","x@x.x",null,"Tal");
    Season season2019 = new Season(2019);
    Season season2020 = new Season(2020);
    Policy policy = new Policy(new SeasonInLeague(league, season2020));

    @BeforeEach
    public void clean() {
        AssociationSystem.getInstance().clearSystem();
        season2019 = new Season(2019);
    }

    @Test
    void testSetPolicyToLeague() {
        season2019.setPolicyToLeague(league,policy);
        assertEquals(season2019.getSeasonLeaguePolicy().get(league),policy);

    }
}
