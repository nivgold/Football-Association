package unit;


import com.domain.logic.AssociationSystem;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.*;
import com.domain.logic.policies.GameSettingPolicy;
import com.domain.logic.policies.Policy;
import com.domain.logic.policies.game_setting_policies.TwoMatchEachPairSettingPolicy;
import com.domain.logic.roles.Referee;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TwoMatchEachPairSettingPolicyTest {

    League league = new League("super league");
    Season season2019 = new Season(2019);
    Policy policy = new Policy(league,season2019);
    Referee referee = new Referee(new Member("","","",null,""));



    @Test
    void testCreateGames() {
        AssociationSystem.getInstance().clearSystem();
        league.addSeason(season2019);
        policy.setGameSettingPolicy(new GameSettingPolicy(policy,new TwoMatchEachPairSettingPolicy()));
        for (int i=0; i<3; i++) {
            Team team = new Team("team" + i, TeamStatus.Open,new TeamOwner(new Member("","","",null,"")),new Field("",",",",",""));
            league.addteamToSeasonInLeague(season2019,team);
        }

        ArrayList<Game> games = policy.getGameSettingPolicy().createGames();
        assertEquals(6,games.size());
    }
}
