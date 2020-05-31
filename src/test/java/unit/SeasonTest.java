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

    private League league = new League("test league");
    private Season season2019 = new Season(2019);

    @BeforeEach
    public void clean() {
        AssociationSystem.getInstance().clearSystem();
        season2019 = new Season(2019);
    }

    @Test
    void testSetPolicyToLeague() {
        SeasonInLeague seasonInLeague = new SeasonInLeague(league, season2019);
        Policy policy = new Policy(seasonInLeague);

        season2019.setPolicyToLeague(league, policy);

        assertEquals(policy, season2019.getSeasonLeaguePolicy().get(league));
    }
}
