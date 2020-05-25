import com.data.DBCommunicator;
import com.domain.domaincontroller.DomainController;
import com.domain.logic.data_types.Address;
import com.domain.logic.enums.PlayerRole;
import com.domain.logic.football.League;
import com.domain.logic.football.Season;
import com.domain.logic.football.Team;
import com.domain.logic.policies.game_setting_policies.OneMatchEachPairSettingPolicy;
import com.domain.logic.policies.game_setting_policies.TwoMatchEachPairSettingPolicy;
import com.domain.logic.roles.*;
import com.domain.logic.users.Guest;
import com.domain.logic.users.Member;
import com.domain.logic.users.SystemManagerMember;
import com.domain.logic.utils.SHA1Function;

import java.util.ArrayList;
import java.util.Date;

import static com.oracle.jrockit.jfr.ContentType.Address;

public class CoverageAcceptance {

    public static void activate(String uc_name) {
        DomainController serviceLayerManager = new DomainController(DBCommunicator.getInstance());
        try {
            serviceLayerManager.performResetSystem();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        switch (uc_name){
            case "1"://mainly focus team management
            {
                /*
                team management includes: add/remove team Owners,
                add/remove teamManagers,
                add/remove Coaches,
                add/remove Players
                set new Field
                 */
                teamManagementAcceptance(serviceLayerManager);
                break;
            }
            case "2"://mainly reset system + removes + complaints + registers
            {
                resetRemovalsRegisters(serviceLayerManager);
                break;
            }
            case "3"://mainly (reset season and league) games (creating game) referees exists...
            {
                Member member = serviceLayerManager.findMember("ref2", SHA1Function.hash("456"));
                AssociationAgent associationAgent = null;
                try {
                    associationAgent = new AssociationAgent(member);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                serviceLayerManager.performCreateLeague(associationAgent, "league1");
                League league = serviceLayerManager.findLeague("league1");
                if (league == null) {
                    System.err.println("League adding by agent failed!");
                } else {
                    System.out.println("League adding by agent succeded");
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                serviceLayerManager.performCreateSeasonInLeague(associationAgent, league, 2019);
                Season season2019 = serviceLayerManager.findSeason(2019);
                if (!season2019.getLeagues().contains(league)) {
                    System.err.println("season wasn't added to league!");
                }
                else {
                    System.out.println("season added properly");
                }
                if (!league.getSeasons().contains(season2019)) {
                    System.err.println("league wasn't added to season!");
                }
                else {
                    System.out.println("league added properly");
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Team team1 = serviceLayerManager.findTeam("team1");
                Team team2 = serviceLayerManager.findTeam("team2");
                serviceLayerManager.performSetGameSettingPolicy(associationAgent,league,season2019,new OneMatchEachPairSettingPolicy());
                if (league.getSeasonLeaguePolicy().get(season2019).getGameSettingPolicy().getSettingStrategy() instanceof OneMatchEachPairSettingPolicy) {
                    System.out.println("setting one match succeeded");
                }
                else {
                    System.err.println("setting one match failed");

                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                serviceLayerManager.performSetGameSettingPolicy(associationAgent,league,season2019,new TwoMatchEachPairSettingPolicy());
                if (league.getSeasonLeaguePolicy().get(season2019).getGameSettingPolicy().getSettingStrategy() instanceof TwoMatchEachPairSettingPolicy) {
                    System.out.println("setting two match policy succeeded");
                }
                else {
                    System.err.println("setting two match policy failed");
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private static void resetRemovalsRegisters(DomainController serviceLayerManager) {
        SystemManagerMember onlySysMan = (SystemManagerMember) serviceLayerManager.findMember("managerSys", SHA1Function.hash("admin"));
        try {
            if (!serviceLayerManager.performRemoveMember(onlySysMan, onlySysMan)) {
                System.out.println("there is only one system manager in the system therefore his deletion didn't succeed");
            }
            else {
                System.err.println("there is only one system manager in the system -> need to prevent deletion");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        SystemManagerMember newSysMan = new SystemManagerMember("korenTest", "blip", "sysEmail", new Address("a", "b", "c", "d"), "mr.Manager");
        try {
            if (serviceLayerManager.performRemoveMember(onlySysMan, newSysMan)) {
                System.out.println("there is more than one system manager in the system therefore his deletion succeeded");
            }
            else {
                System.err.println("there is more than one system manager in the system -> didn't need to prevent deletion");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        ArrayList<Team> allTeams = serviceLayerManager.getALLTeams();
        int counter = 100;
        for(Team team : allTeams){
            TeamOwner teamOwner = team.getTeam_owners().get(0);
            Member memTeamOwner = teamOwner.getMember();
            try {
                if (!serviceLayerManager.performRemoveMember(onlySysMan, memTeamOwner)) {
                    System.out.println("team owner " + teamOwner.getMember().getName() + "can't be deleted" +
                            " cause he is the only team owner of the team: " + team.getTeamName());
                    if(serviceLayerManager.findMember(memTeamOwner) == null ){
                        System.err.println("team owner meed to be in the ManageMembers hash");
                    }
                    if (serviceLayerManager.findTeam(team.getTeamName()) == null) {
                        System.err.println("the team " + team.getTeamName()+ " should stay in the system, but was deleted");
                    }
                    else {
                        System.out.println("the team " + team.getTeamName()+ " stayed in the system");
                    }
                    //TODO check good addition of team owner...

                    Guest guest = new Guest("guestNameo", "lastNameo");
                    Member added = serviceLayerManager.performRegisterAsMember(guest, "guesto" + counter, "pesto" + counter, "Email", "q", "w", "t", "jf");
                    Player player = new Player(added, new Date());
                    teamOwner.addPlayer(player, PlayerRole.CB);
                    if(team.getPlayers().get(0).getPlayer() == player){
                        System.out.println("team and player are indeed connected");
                    }
                    else{
                        System.err.println("team and player are supposed to be connected");
                    }
                }
                else {
                    System.err.println("team owner " + teamOwner.getMember().getName() +" was deleted. he is the only owner of his team so he can't be deleted" +
                            "!" );
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            counter++;
        }
        Member memAssociationAgent = serviceLayerManager.findMember("ass", SHA1Function.hash("a123"));
        ArrayList<IRole> allRoles = memAssociationAgent.getRoles();
        AssociationAgent associationAgent = (AssociationAgent) allRoles.get(0);
        associationAgent.createLeague("newLeague");
        if(serviceLayerManager.findLeague("newLeague") != null){
            System.out.println("association agent added new league to the system");
        }
        else{
            System.err.println("association agent added new league to the system");
        }

    }

    private static void teamManagementAcceptance(DomainController serviceLayerManager){
        Member memberOwner1 = serviceLayerManager.findMember("owner1", SHA1Function.hash("owner1"));
        TeamOwner owner1 = (TeamOwner) memberOwner1.getRoles().get(0);
        System.out.println("niv5 is the team owner of the team1");
        Member member2 = serviceLayerManager.findMember("ref2", SHA1Function.hash("456"));
        System.out.println("niv5 wants to appoint tal to be another team owner in team1");
        try {
            serviceLayerManager.performAppointTeamOwner(owner1, member2);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        // tests for successful team owner appointment
        if (member2.getRoles().size()==0){
            System.err.println("tal was not appointed successfully as team owner in team1");
            return;
        }

        if (member2.getRoles().get(1).getClass() != TeamOwner.class){
            System.err.println("tal was not appointed successfully as team owner in team1");
            return;
        }

        TeamOwner owner2 = (TeamOwner) member2.getRoles().get(1);
        if (owner2.getAppointer()!=owner1){
            System.err.println("tal was not appointed successfully as team owner in team1");
            return;
        }
        if (!owner1.getAppointments().contains(owner2)){
            System.err.println("tal was not appointed successfully as team owner in team1");
            return;
        }
        if (owner2.getTeam()!=owner1.getTeam()){
            System.err.println("tal was not appointed successfully as team owner in team1");
            return;
        }
        System.out.println("tal is now a team owner in team1");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();

        Member memberOwner3 = serviceLayerManager.findMember("owner2", SHA1Function.hash("owner2"));
        TeamOwner owner3 = (TeamOwner)memberOwner3.getRoles().get(0);
        System.out.println("niv6 is the team owner of team2");
        System.out.println("niv6 want also to appoint tal as team owner in team2");
        try {
            serviceLayerManager.performAppointTeamOwner(owner3, member2);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        // tests for the failure of adding tal as team owner in team 2
        if (member2.getRoles().size()!=2){
            System.err.println("tal is now a team owner in team2");
            return;
        }
        if (((TeamOwner) member2.getRoles().get(1)).getTeam()!=owner1.getTeam()){
            System.err.println("tal is now a team owner in team2");
            return;
        }
        if (((TeamOwner) member2.getRoles().get(1)).getAppointer() != owner1){
            System.err.println("tal is now a team owner in team2");
            return;
        }
        if (owner3.getAppointments().size()!=0) {
            System.err.println("tal is now a team owner in team2");
            return;
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();

        // tests for removal of team owner
        System.out.println("tal want to remove niv5 from being a team owner in team 1");
        try {
            serviceLayerManager.performRemoveTeamOwner(owner2, owner1);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        if (memberOwner1.getRoles().size()!=1){
            System.err.println("tal removed niv5 from being team owner in team1");
            return;
        }
        if (memberOwner1.getRoles().get(0).getClass() != TeamOwner.class){
            System.err.println("tal removed niv5 from being team owner in team1");
            return;
        }
        if (owner1 != memberOwner1.getRoles().get(0)){
            System.err.println("tal removed niv5 from being team owner in team1");
            return;
        }
        if (owner2.getTeam() != owner1.getTeam()){
            System.err.println("tal removed niv5 from being team owner in team1");
            return;
        }
        if (owner2.getAppointer()!=owner1){
            System.err.println("tal removed niv5 from being team owner in team1");
            return;
        }
        if (owner1.getAppointments().size()!=1){
            System.err.println("tal removed niv5 from being team owner in team1");
            return;
        }
        System.out.println("tal can't remove niv5 from being team owner in team 1");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();

        // tests for successful removal of team owner tal
        System.out.println("niv5 wants to remove tal from being team owner in team1");
        try {
            serviceLayerManager.performRemoveTeamOwner(owner1, owner2);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        if (member2.getRoles().size()!=1){
            System.err.println("tal was NOT removed from being team owner in team 1");
            return;
        }
        if (owner2.getTeam()!=null){
            System.err.println("tal was NOT removed from being team owner in team 1");
            return;
        }
        if (owner2.getAppointer()!=null){
            System.err.println("tal was NOT removed from being team owner in team 1");
            return;
        }
        if (owner1.getAppointments().size()!=0){
            System.err.println("tal was NOT removed from being team owner in team 1");
            return;
        }
        System.out.println("tal is now NOT a team owner in team 1");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();

        // tests for adding team manager
        System.out.println("niv6 wants to appoint tal as team manager in team2");
        try {
            serviceLayerManager.performAppointTeamManager(owner3, member2);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (member2.getRoles().size()!=2){
            System.err.println("tal was not appointed successfully as team manager in team2");
            return;
        }
        if (member2.getRoles().get(1).getClass() != TeamManager.class){
            System.err.println("tal was not appointed successfully as team manager in team2");
            return;
        }
        TeamManager manager2 = (TeamManager) member2.getRoles().get(1);
        if (manager2.getAppointer()!=owner3){
            System.err.println("tal was not appointed successfully as team manager in team2");
            return;
        }
        if (manager2.getTeam()!=owner3.getTeam()){
            System.err.println("tal was not appointed successfully as team manager in team2");
            return;
        }
        if (owner3.getAppointments().size()!=1){
            System.err.println("tal was not appointed successfully as team manager in team2");
            return;
        }
        if (!owner3.getAppointments().contains(manager2)){
            System.err.println("tal was not appointed successfully as team manager in team2");
            return;
        }
        System.out.println("tal is now a team manager in team2");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();

        // tests for failure of appoint team manager tal to team1
        System.out.println("niv5 wants to appoint tal to be team manager in team1");
        try {
            serviceLayerManager.performAppointTeamManager(owner1, member2);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (owner1.getAppointments().contains(manager2)){
            System.err.println("tal is also a team manager in team1");
            return;
        }
        if (member2.getRoles().size()!=2){
            System.err.println("tal is also a team manager in team1");
            return;
        }
        if (manager2.getTeam()==owner1.getTeam()){
            System.err.println("tal is also a team manager in team1");
            return;
        }
        if (manager2.getAppointer()==owner1){
            System.err.println("tal is also a team manager in team1");
            return;
        }
        System.out.println("tal cannot be a team manager also in team1");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();

        // tests for failure removal of team manager
        System.out.println("niv5, the team owner of team1 wants to remove tal from being team manager in team1");
        try {
            serviceLayerManager.performRemoveTeamManager(owner1, manager2);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (manager2.getTeam()==null){
            System.err.println("tal was removed from being team manager in team 1");
            return;
        }
        if (manager2.getAppointer()==null){
            System.err.println("tal was removed from being team manager in team 1");
            return;
        }
        System.out.println("tal cannot be removed from being a team manager in team1");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
        System.out.println();

        // test for successful removal of team manager
        System.out.println("niv6, the team manager of team2 wants to remove tal from being team manger from team2");
        try {
            serviceLayerManager.performRemoveTeamManager(owner3, manager2);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (manager2.getAppointer()!=null){
            System.err.println("tal was not removed successfully from being team manager in team2");
            return;
        }
        if (manager2.getTeam()!=null){
            System.err.println("tal was not removed successfully from being team manager in team2");
            return;
        }
        if (owner3.getAppointments().size()!=0){
            System.err.println("tal was not removed successfully from being team manager in team2");
            return;
        }
        System.out.println("tal is now NOT a team manager in team2");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();

        Member member4 = serviceLayerManager.findMember("ref1", SHA1Function.hash("123"));
        // tests for appoint coach
        System.out.println("niv5 wants to appoint koren as coach in team1");
        try {
            serviceLayerManager.performAppointCoach(owner1, member4);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (member4.getRoles().size()!=2){
            System.err.println("koren was not appointed successfully as coach in team1");
            return;
        }
        if (member4.getRoles().get(1).getClass() != Coach.class){
            System.err.println("koren was not appointed successfully as coach in team1");
            return;
        }
        Coach coach4 = (Coach)member4.getRoles().get(1);
        if (coach4.getTeams().size()!=1){
            System.err.println("koren was not appointed successfully as coach in team1");
            return;
        }
        if (coach4.getTeams().get(0).getTeam()!=owner1.getTeam()){
            System.err.println("koren was not appointed successfully as coach in team1");
            return;
        }
        System.out.println("koren is now a coach in team1");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();

        // tests for failure removal of coach
        System.out.println("niv6, the team owner of team2, wants to remove koren from being a coach in his team");
        try {
            serviceLayerManager.performRemoveCoach(owner3, coach4);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (coach4.getTeams().size()!=1){
            System.err.println("koren is not a coach anymore");
            return;
        }
        if (coach4.getTeams().get(0)==null){
            System.err.println("koren is not a coach anymore");
            return;
        }
        if (coach4.getTeams().get(0).getTeam()==null){
            System.err.println("koren is not a coach anymore");
            return;
        }
        System.out.println("niv6 can't remove koren from being a coach in team2");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();

        //tests for successful removal of coach
        System.out.println("niv5 wants to remove koren from being a coach in team1");
        try {
            serviceLayerManager.performRemoveCoach(owner1, coach4);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (coach4.getTeams().size()!=0){
            System.err.println("koren was not successfully removed from being a coach");
            return;
        }
        if (member4.getRoles().size()!=1){
            System.err.println("koren was not successfully removed from being a coach");
            return;
        }
        System.out.println("koren is not a coach anymore in team1");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();

        // adding player
        System.out.println("niv5 wants to add koren as player in team1");
        try {
            serviceLayerManager.performAddPlayer(owner1, member4, PlayerRole.CAM);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (member4.getRoles().size()!=2){
            System.err.println("koren is not a player in team1");
            return;
        }
        if (member4.getRoles().get(1).getClass() != Player.class){
            System.err.println("koren is not a player in team1");
            return;
        }
        Player player = (Player)member4.getRoles().get(1);
        if (player.getRoleInTeams().size()!=1){
            System.err.println("koren is not a player in team1");
            return;
        }
        if (player.getRoleInTeams().get(0)==null){
            System.err.println("koren is not a player in team1");
            return;
        }
        if (player.getRoleInTeams().get(0).getTeam()!=owner1.getTeam()){
            System.err.println("koren is not a player in team1");
            return;
        }
        System.out.println("koren is now a player in team1");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();

        // tests for failure removal of player
        System.out.println("niv6 wants to remove koren from being a player in team2");
        try {
            serviceLayerManager.performRemovePlayer(owner3, player);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (player.getRoleInTeams().size()!=1){
            System.err.println("koren was removed from being a player in team 2");
            return;
        }
        if (player.getRoleInTeams().get(0) == null){
            System.err.println("koren was removed from being a player in team 2");
            return;
        }
        if (player.getRoleInTeams().get(0).getTeam()==null){
            System.err.println("koren was removed from being a player in team 2");
            return;
        }
        System.out.println("niv6 cannot remove koren from being a player in team2");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();

        //tests for successful removal of player
        System.out.println("niv5 wants to remove koren from being a player in team1");
        try {
            serviceLayerManager.performRemovePlayer(owner1, player);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (player.getRoleInTeams().size()!=0){
            System.err.println("koren was not removed successfully from being a player in team 1");
            return;
        }
        if (member4.getRoles().size()!=1){
            System.err.println("koren was not removed successfully from being a player in team 1");
            return;
        }
        System.out.println("koren is not a player anymore in team1");
    }
}
