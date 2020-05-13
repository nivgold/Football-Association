package unit;


import com.domain.logic.AssociationSystem;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.*;
import com.domain.logic.policies.GameSettingPolicy;
import com.domain.logic.policies.Policy;
import com.domain.logic.policies.game_setting_policies.OneMatchEachPairSettingPolicy;
import com.domain.logic.roles.Referee;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OneMatchEachPairSettingPolicyTest {

    League league = new League("super league");
    Season season2019 = new Season(2019);
    Policy policy = new Policy(new SeasonInLeague(league, season2019));
    Referee referee;

    {
        try {
            referee = new Referee(new Member("","","",null,""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    void testCreateGames() {
        AssociationSystem.getInstance().clearSystem();
        league.addSeason(season2019);
        policy.setGameSettingPolicy(new GameSettingPolicy(policy,new OneMatchEachPairSettingPolicy()));
        for (int i=0; i<3; i++) {
            Team team = null;
            try {
                team = new Team("team" + i, TeamStatus.Open,new TeamOwner(new Member("","","",null,"")),new Field("",",",",",""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            league.addTeamToSeasonInLeague(season2019,team);
        }

        ArrayList<Game> games = policy.getGameSettingPolicy().createGames();
        assertEquals(3,games.size());
    }
}
