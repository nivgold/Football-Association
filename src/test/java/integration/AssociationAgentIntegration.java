package integration;

import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.football.League;
import com.domain.logic.football.Season;
import com.domain.logic.policies.GameSettingPolicy;
import com.domain.logic.policies.RankingPolicy;
import com.domain.logic.policies.game_setting_policies.OneMatchEachPairSettingPolicy;
import com.domain.logic.roles.AssociationAgent;
import com.domain.logic.roles.Coach;
import com.domain.logic.roles.IRole;
import com.domain.logic.roles.Referee;
import com.domain.logic.users.Member;
import com.domain.logic.utils.SHA1Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class AssociationAgentIntegration {

    private AssociationAgent associationAgent;
    private AssociationSystem system;

    @BeforeEach
    public void beforeTestMethod(){
        AssociationSystem.getInstance().clearSystem();
        Member member = new Member("agent", SHA1Function.hash("agent"), "agent@gmail.com", new Address("Israel", "Israel", "Haifa", "3189240"), "moshe");
        try {
            this.associationAgent = new AssociationAgent(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.system = AssociationSystem.getInstance();
        League league = new League("league1");
        Season season = new Season(2019);
        league.addSeason(season);
    }

    @Test
    public void testCreateLeague() {
        this.associationAgent.createLeague("league2");
        assertEquals(2, this.system.getManageLeagues().getAllLeagues().size());

        this.associationAgent.createLeague("league1");
        assertEquals(2, this.system.getManageLeagues().getAllLeagues().size());
    }

    @Test
    public void testCreateSeasonInLeague(){
        League league1 = this.system.getManageLeagues().findLeague("league1");
        this.associationAgent.createSeasonInLeague(league1, 2012);
        assertEquals(2012, this.system.getManageSeasons().findSeason(2012).getYear());
        assertEquals(2, league1.getSeasons().size());
        assertEquals(2012, league1.getSeasons().get(1).getYear());

        League league2 = new League("league2");
        this.associationAgent.createSeasonInLeague(league2, 2012);
        assertEquals(1, league2.getSeasons().size());
        assertEquals(2012, league2.getSeasons().get(0).getYear());

        // season already exists
        this.associationAgent.createSeasonInLeague(league1, 2012);
        assertEquals(2, league1.getSeasons().size());
    }

    @Test
    public void testAppointReferee(){
        Member member = new Member("referee", SHA1Function.hash("referee"), "referee@gmail.com", new Address("Israel", "Israel", "Naharia", "2919056"), "yossi");

        try {
            this.associationAgent.appointReferee(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(Referee.class, member.getRoles().get(0).getClass());

        try {
            this.associationAgent.appointReferee(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, member.getRoles().size());
    }

    @Test
    public void testRemoveReferee(){
        Member member = new Member("referee", SHA1Function.hash("referee"), "referee@gmail.com", new Address("Israel", "Israel", "Naharia", "2919056"), "yossi");
        try {
            this.associationAgent.appointReferee(member);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.associationAgent.removeReferee((Referee)member.getRoles().get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (IRole role : member.getRoles()){
            assertFalse(role instanceof Referee);
        }
    }

    @Test
    public void testSetRefereeInLeague(){
        Member member = new Member("referee", SHA1Function.hash("referee"), "referee@gmail.com", new Address("Israel", "Israel", "Naharia", "2919056"), "yossi");
        try {
            this.associationAgent.appointReferee(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Referee referee = (Referee)member.getRoles().get(0);

        League league1 = this.system.getManageLeagues().findLeague("league1");
        Season season1 = league1.getSeasons().get(0);
        Season season2 = new Season(2015);

        this.associationAgent.setRefereeInLeague(referee, league1, season2);
        assertEquals(0, league1.getLeagueRefereeMap().size());

        this.associationAgent.setRefereeInLeague(referee, league1, season1);
        assertEquals(1, league1.getLeagueRefereeMap().size());
        assertEquals(referee, league1.getLeagueRefereeMap().get(season1).get(0));

        this.associationAgent.setRefereeInLeague(referee, league1, season1);
        assertEquals(1, league1.getLeagueRefereeMap().size());
    }

    @Test
    public void testSetRankingPolicy(){
        League league = this.system.getManageLeagues().findLeague("league1");
        Season season = league.getSeasons().get(0);
        RankingPolicy rankingPolicy = new RankingPolicy(league.getSeasonLeaguePolicy().get(season), 5, 1, 0, 0, 0);

        // setting Ranking Policy successfully
        try {
            this.associationAgent.setRankingPolicy(league, season, rankingPolicy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(rankingPolicy, league.getSeasonLeaguePolicy().get(season).getRankingPolicy());
    }

    @Test
    public void testSetGameSettingPolicy(){
        League league = this.system.getManageLeagues().findLeague("league1");
        Season season = league.getSeasons().get(0);
        GameSettingPolicy gameSettingPolicy = new GameSettingPolicy(league.getSeasonLeaguePolicy().get(season), new OneMatchEachPairSettingPolicy());
        try {
            // setting Game Setting Policy successfully
            this.associationAgent.setGameSettingPolicy(league, season, gameSettingPolicy);
            assertEquals(gameSettingPolicy, league.getSeasonLeaguePolicy().get(season).getGameSettingPolicy());

            // same Game Setting Policy already defined
            this.associationAgent.setGameSettingPolicy(league, season, new GameSettingPolicy(league.getSeasonLeaguePolicy().get(season), new OneMatchEachPairSettingPolicy()));
            assertEquals(gameSettingPolicy, league.getSeasonLeaguePolicy().get(season).getGameSettingPolicy());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
