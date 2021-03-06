package integration;

import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.football.League;
import com.domain.logic.football.Season;
import com.domain.logic.policies.GameSettingPolicy;
import com.domain.logic.policies.RankingPolicy;
import com.domain.logic.policies.game_setting_policies.OneMatchEachPairSettingPolicy;
import com.domain.logic.roles.IRole;
import com.domain.logic.roles.*;
import com.domain.logic.users.Member;
import com.domain.logic.utils.SHA1Function;
import com.stubs.DBStub;
import com.stubs.AssociationAgentStub;
import com.stubs.LeagueStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class AssociationAgentIntegration {

    private AssociationAgentStub associationAgent;
    private AssociationSystem system;
    private DBStub dbStub = DBStub.getInstance();

    @BeforeEach
    public void beforeTestMethod() {
        AssociationSystem.getInstance().clearSystem();
        Member member = new Member("agent", SHA1Function.hash("agent"), "agent@gmail.com", new Address("Israel", "Israel", "Haifa", "3189240"), "moshe");
        dbStub.addMember(member);
        try {
            this.associationAgent = new AssociationAgentStub(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.system = AssociationSystem.getInstance();
        League league = new League("league1");
        dbStub.addLeague(league);
        Season season = new Season(2019);
        dbStub.addSeason(season);
        league.addSeason(season);
        try {
            dbStub.addSeasonInLeague(2019, "league1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateLeague() throws Exception {
        this.associationAgent.createLeague("league2");
        assertEquals(2, dbStub.getAllLeaguesNames().size());

        this.associationAgent.createLeague("league1");
        assertEquals(2, dbStub.getAllLeaguesNames().size());
    }

    @Test
    public void testCreateSeasonInLeague() {
        League league1 = null;
        try {
            league1 = dbStub.findLeague("league1");
            this.associationAgent.createSeasonInLeague(league1, 2019);
            assertEquals(2019, dbStub.findSeason(2019).getYear());
            assertEquals(2, league1.getSeasons().size());
            assertEquals(2019, league1.getSeasons().get(1).getYear());
            associationAgent.createLeague("league2");
            League league2 = dbStub.findLeague("league2");
            this.associationAgent.createSeasonInLeague(league2, 2019);
            assertEquals(1, league2.getSeasons().size());
            assertEquals(2019, league2.getSeasons().get(0).getYear());

            // season already exists
            this.associationAgent.createSeasonInLeague(league1, 2019);
            assertEquals(2, league1.getSeasons().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAppointReferee() {
        Member member = new Member("referee", SHA1Function.hash("referee"), "referee@gmail.com", new Address("Israel", "Israel", "Naharia", "2919056"), "yossi");
        dbStub.addMember(member);
        try {
            member = dbStub.findMember(member.getUserName());
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
    public void testRemoveReferee() {
        Member member = new Member("referee", SHA1Function.hash("referee"), "referee@gmail.com", new Address("Israel", "Israel", "Naharia", "2919056"), "yossi");
        try {
            dbStub.addMember(member);
            this.associationAgent.appointReferee(member);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.associationAgent.removeReferee((Referee) member.getRoles().get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (IRole role : member.getRoles()) {
            assertFalse(role instanceof Referee);
        }
    }

//    @Test
//    public void testSetRefereeInLeague() {
//        Member member = new Member("referee", SHA1Function.hash("referee"), "referee@gmail.com", new Address("Israel", "Israel", "Naharia", "2919056"), "yossi");
//        try {
//            dbStub.addMember(member);
//            this.associationAgent.appointReferee(member);
//            Referee referee = (Referee) member.getSpecificRole(Referee.class);
//
//            League league1 = null;
//            league1 = dbStub.findLeague("league1");
//            Season season1 = league1.getSeasons().get(0);
//            Season season2 = new Season(2015);
//            dbStub.addSeason(season2);
//
//            this.associationAgent.setRefereeInLeague(referee, league1, season2);
//            assertEquals(1, league1.getLeagueRefereeMap().size());
//
//            this.associationAgent.setRefereeInLeague(referee, league1, season1);
//            assertEquals(1, league1.getLeagueRefereeMap().size());
//            assertEquals(referee, league1.getLeagueRefereeMap().get(season1).get(0));
//
//            this.associationAgent.setRefereeInLeague(referee, league1, season1);
//            assertEquals(1, league1.getLeagueRefereeMap().size());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    public void testSetRankingPolicy() throws Exception {
        dbStub.addLeague(new LeagueStub("po"));
        League league = dbStub.findLeague("po");
        Season season = league.getSeasons().get(0);
        dbStub.addSeason(season);
        dbStub.addSeasonInLeague(season.getYear(), "po");
        RankingPolicy rankingPolicy = new RankingPolicy(league.getSeasonLeaguePolicy().get(season), 5, 1, 0, 0, 0);

        // setting Ranking Policy successfully
        try {
            this.associationAgent.setRankingPolicy(league, season, rankingPolicy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(rankingPolicy.getWin(), league.getSeasonLeaguePolicy().get(season).getRankingPolicy().getWin());
        assertEquals(rankingPolicy.getDraw(), league.getSeasonLeaguePolicy().get(season).getRankingPolicy().getDraw());
        assertEquals(rankingPolicy.getGoals(), league.getSeasonLeaguePolicy().get(season).getRankingPolicy().getGoals());
    }

    @Test
    public void testSetGameSettingPolicy() throws Exception {
        dbStub.addLeague(new LeagueStub("po"));
        League league = dbStub.findLeague("po");
        Season season = league.getSeasons().get(0);
        dbStub.addSeason(season);
        dbStub.addSeasonInLeague(season.getYear(), "po");
        GameSettingPolicy gameSettingPolicy = new GameSettingPolicy(league.getSeasonLeaguePolicy().get(season), new OneMatchEachPairSettingPolicy());
        try {
            // setting Game Setting Policy successfully
            this.associationAgent.setGameSettingPolicy(league, season, gameSettingPolicy);
            assertEquals(gameSettingPolicy.getSettingStrategy().getClass(), league.getSeasonLeaguePolicy().get(season).getGameSettingPolicy().getSettingStrategy().getClass());

            // same Game Setting Policy already defined
            assertEquals(gameSettingPolicy.getSettingStrategy().getClass(), league.getSeasonLeaguePolicy().get(season).getGameSettingPolicy().getSettingStrategy().getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
