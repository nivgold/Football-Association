import com.data.DBCommunicator;
import com.domain.domaincontroller.DomainController;
import com.domain.logic.football.League;
import com.domain.logic.football.Season;
import com.domain.logic.football.SeasonInLeague;
import com.domain.logic.managers.ManageLeagues;
import com.domain.logic.policies.GameSettingPolicy;
import com.domain.logic.policies.Policy;
import com.domain.logic.policies.game_setting_policies.OneMatchEachPairSettingPolicy;
import com.domain.logic.policies.game_setting_policies.TwoMatchEachPairSettingPolicy;
import com.domain.logic.roles.AssociationAgent;
import com.domain.logic.roles.IRole;
import com.domain.logic.roles.Referee;
import com.domain.logic.users.Member;

public class AssociationAgentAcceptanceTest {
    private static String assosName = "assocAgent yarin";

    public AssociationAgentAcceptanceTest() {
    }

    public static void activate(String UC_NAME){
        DomainController serviceLayerManager = new DomainController(DBCommunicator.getInstance());
        serviceLayerManager.login("sysManager_admin", "admin", "", "");
        serviceLayerManager.login("assocAgent yarin", "yarinpass", "", "");
        try {
            serviceLayerManager.performResetSystem("sysManager_admin");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
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

            case ("5.6") : {//checks setting game policy
                SeasonInLeague seasonInLeague = serviceLayerManager.findSeasonInLeague(2020, "Base League");
                try {
                    serviceLayerManager.defineGameSettingPolicy(assosName, seasonInLeague.getLeague().getLeagueName(), seasonInLeague.getSeason().getYear(), "two");
                    seasonInLeague = serviceLayerManager.findSeasonInLeague(2020, "Base League");
                    if(seasonInLeague.getPolicy().getGameSettingPolicy().getSettingStrategy() instanceof TwoMatchEachPairSettingPolicy){
                        System.out.println("setting the game setting policy was successful");
                    }
                    else{
                        System.err.println("failed to setting the game setting policy was successful");
                    }
                    //again two
                    serviceLayerManager.defineGameSettingPolicy(assosName, seasonInLeague.getLeague().getLeagueName(), seasonInLeague.getSeason().getYear(), "two");
                    seasonInLeague = serviceLayerManager.findSeasonInLeague(2020, "Base League");
                    if(seasonInLeague.getPolicy().getGameSettingPolicy().getSettingStrategy() instanceof TwoMatchEachPairSettingPolicy){
                        System.out.println("setting the game setting policy was successful");
                    }
                    else{
                        System.err.println("failed to setting the game setting policy was successful");
                    }
                    //change to one
                    serviceLayerManager.defineGameSettingPolicy(assosName, seasonInLeague.getLeague().getLeagueName(), seasonInLeague.getSeason().getYear(), "one");
                    seasonInLeague = serviceLayerManager.findSeasonInLeague(2020, "Base League");
                    if(seasonInLeague.getPolicy().getGameSettingPolicy().getSettingStrategy() instanceof OneMatchEachPairSettingPolicy){
                        System.out.println("setting the game setting policy was successful");
                    }
                    else{
                        System.err.println("failed to setting the game setting policy was successful");
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                break;
            }

            case ("5.7") : {//checks setting ranking game policy
                SeasonInLeague seasonInLeague = serviceLayerManager.findSeasonInLeague(2020, "Base League");
                try {
                    serviceLayerManager.defineGameRankingPolicy(assosName, seasonInLeague.getLeague().getLeagueName(), seasonInLeague.getSeason().getYear(), 1, 1, 1, 1, 1);
                    seasonInLeague = serviceLayerManager.findSeasonInLeague(2020, "Base League");
                    if(seasonInLeague.getPolicy().getRankingPolicy().getWin() == 1){
                        System.out.println("setting the ranking game setting policy was successful");
                    }
                    else{
                        System.err.println("failed to setting the ranking game setting policy");
                    }
                    //again two
                    serviceLayerManager.defineGameRankingPolicy(assosName, seasonInLeague.getLeague().getLeagueName(), seasonInLeague.getSeason().getYear(), 1, 1, 1, 1, 1);
                    seasonInLeague = serviceLayerManager.findSeasonInLeague(2020, "Base League");
                    if(seasonInLeague.getPolicy().getRankingPolicy().getWin() == 1){
                        System.out.println("setting the ranking game setting policy was successful");
                    }
                    else{
                        System.err.println("failed to setting the ranking game setting policy");
                    }
                    //change to one
                    serviceLayerManager.defineGameRankingPolicy(assosName, seasonInLeague.getLeague().getLeagueName(), seasonInLeague.getSeason().getYear(), 2, 2, 2,  2, 2);
                    seasonInLeague = serviceLayerManager.findSeasonInLeague(2020, "Base League");
                    if(seasonInLeague.getPolicy().getRankingPolicy().getWin() == 2){
                        System.out.println("setting the ranking game setting policy was successful");
                    }
                    else{
                        System.err.println("failed to setting the ranking game setting policy");
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                break;
            }

            default: {
                System.out.println("Wrong association agent UC code.");
            }
        }

    }
}
