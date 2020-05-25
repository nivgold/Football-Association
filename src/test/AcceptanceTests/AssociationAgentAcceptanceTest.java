import com.data.DBCommunicator;
import com.domain.domaincontroller.DomainController;
import com.domain.logic.football.League;
import com.domain.logic.football.Season;
import com.domain.logic.football.SeasonInLeague;
import com.domain.logic.managers.ManageLeagues;
import com.domain.logic.policies.GameSettingPolicy;
import com.domain.logic.policies.Policy;
import com.domain.logic.policies.RankingPolicy;
import com.domain.logic.roles.AssociationAgent;
import com.domain.logic.roles.IRole;
import com.domain.logic.roles.Referee;
import com.domain.logic.users.Member;

public class AssociationAgentAcceptanceTest {

    public AssociationAgentAcceptanceTest() {
    }

    public static void activate(String UC_NAME){
        DomainController serviceLayerManager = new DomainController(DBCommunicator.getInstance());
        AssociationAgent associationAgent = null;
        try {
            associationAgent = new AssociationAgent(new Member("","","",null,"name"));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        League league = new League("lame league");
        Season season2020 = new Season(2020);
        Member member = new Member("moshe","123","moshe@",null,"moshe datz");

        switch (UC_NAME) {

            case ("5.1"): {
                String chosenName = "test league";
                serviceLayerManager.performCreateLeague(associationAgent,chosenName);
                ManageLeagues manageLeagues = ManageLeagues.getInstance();
                if (manageLeagues.findLeague(chosenName)==null) {
                    System.err.println("league adding failed");
                }
                break;
            }

            case ("5.2"): {
                int selectedYear = 1999;
                League selectedLeague = league;
                serviceLayerManager.performCreateSeasonInLeague(associationAgent,selectedLeague,selectedYear);
                break;
            }

            case ("5.3"): {
                try {
                    serviceLayerManager.performAppointReferee(associationAgent,member);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                boolean ok = false;
                for (IRole role : member.getRoles()) {
                    if (role instanceof Referee)
                        ok = true;
                }
                if (!ok)
                    System.err.println("Appointing referee failed!");
                break;
            }

            case ("5.4") : {
                Referee referee = null;
                try {
                    referee = new Referee(member);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                try {
                    serviceLayerManager.performRemoveReferee(associationAgent,referee);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                if (member.getRoles().contains(referee)) {
                    System.err.println("removing referee failed- still a role of themember");
                }
                break;
            }

            case ("5.5") : {
                Referee referee = null;
                try {
                    referee = new Referee(member);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                serviceLayerManager.performSetRefereeInLeagueInSeason(associationAgent,referee,league,season2020);
                if (!league.getLeagueRefereeMap().get(season2020).contains(referee)) {
                    System.err.println("connecting referee failed- not in league's map");
                }
                break;
            }

            case ("5.6") : {
                SeasonInLeague seasonInLeague = new SeasonInLeague(league, season2020);
                Policy policy = new Policy(seasonInLeague);
                RankingPolicy chosenRP = new RankingPolicy(policy);
                try {
                    league.setPolicyToSeason(season2020,policy);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                serviceLayerManager.performSetRankingPolicy(associationAgent,league,season2020,chosenRP);
                if (!(policy.getRankingPolicy()==chosenRP))
                    System.err.println("setting ranking policy failed");
                break;
            }

            case ("5.7") : {
                SeasonInLeague seasonInLeague = new SeasonInLeague(league, season2020);
                Policy policy = new Policy(seasonInLeague);
                GameSettingPolicy chosenGS = new GameSettingPolicy(policy,null);
                try {
                    league.setPolicyToSeason(season2020,policy);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                serviceLayerManager.performSetGameSettingPolicy(associationAgent,league,season2020,null);
                if (!(policy.getGameSettingPolicy()==chosenGS))
                    System.err.println("setting game setting policy failed");
                break;
            }

            default: {
                System.out.println("Wrong association agent UC code.");
            }
        }

    }
}
