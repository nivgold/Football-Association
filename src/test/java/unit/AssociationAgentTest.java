package unit;


import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.football.League;
import com.domain.logic.football.Season;
import com.domain.logic.policies.GameSettingPolicy;
import com.domain.logic.policies.RankingPolicy;
import com.domain.logic.policies.game_setting_policies.OneMatchEachPairSettingPolicy;
import com.domain.logic.roles.AssociationAgent;
import com.domain.logic.roles.Referee;
import com.domain.logic.users.Member;
import com.domain.logic.utils.SHA1Function;
import com.stubs.LeagueStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


public class AssociationAgentTest {
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
        this.system.getManageLeagues().addLeague(new LeagueStub("league1"));
    }

    @Test
    public void createLeague() {
        // adding league successfully
        this.associationAgent.createLeague("league2");
        assertEquals("league2",system.getManageLeagues().getAllLeagues().get(0).getLeagueName());

        // already exist
        this.associationAgent.createLeague("league1");
        assertEquals(2, system.getManageLeagues().getAllLeagues().size());
    }

    @Test
    public void createSeasonInLeague() {
        // league does not exist
        League league2 = new League("league2");
        this.associationAgent.createSeasonInLeague(league2, 2012);

        // season already exist in league
        League league1 = system.getManageLeagues().findLeague("league1");
        this.associationAgent.createSeasonInLeague(league1, 2012);
        assertEquals(1,  league1.getSeasons().size());

        // successfully adding season to league
        this.associationAgent.createSeasonInLeague(league1, 2015);
        assertEquals(2, league1.getSeasons().size());
        assertEquals(2015, league1.getSeasons().get(1).getYear());
    }

    @Test
    public void appointReferee() {
        Member member = new Member("referee", SHA1Function.hash("referee"), "referee@gmail.com", new Address("Israel", "Israel", "Naharia", "2919056"), "yossi");

        // adding member as Referee successfully
        try {
            this.associationAgent.appointReferee(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(Referee.class, member.getRoles().get(0).getClass());

        // member already a Referee
        try {
            this.associationAgent.appointReferee(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, member.getRoles().size());
    }

    @Test
    public void setRefereeInLeague() {
        Member member = new Member("referee", SHA1Function.hash("referee"), "referee@gmail.com", new Address("Israel", "Israel", "Naharia", "2919056"), "yossi");
        Referee referee = null;
        try {
            referee = new Referee(member);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // season not exist in league
        League league1 = system.getManageLeagues().findLeague("league1");
        Season season2 = new Season(2015);
        this.associationAgent.setRefereeInLeague(referee, league1, season2);
        assertEquals(0, league1.getLeagueRefereeMap().size());

        // adding successfully the referee to the season in the league
        Season season1 = league1.getSeasons().get(0);
        this.associationAgent.setRefereeInLeague(referee, league1, season1);
        assertEquals(1, league1.getLeagueRefereeMap().get(season1).size());
        assertEquals(referee, league1.getLeagueRefereeMap().get(season1).get(0));

        // Referee already exists
        this.associationAgent.setRefereeInLeague(referee, league1, season1);
        assertEquals(1, league1.getLeagueRefereeMap().get(season1).size());
    }

    @Test
    public void setRankingPolicy() {
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
    public void setGameSettingPolicy() {
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

    @Test
    public void removeYourself() {
        Member member = this.associationAgent.getMember();

        try {
            this.associationAgent.removeYourself();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(0, member.getRoles().size());
    }
}