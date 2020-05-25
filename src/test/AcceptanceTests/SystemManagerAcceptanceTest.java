import com.data.DBCommunicator;
import com.domain.domaincontroller.DomainController;
import com.domain.logic.AssociationSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.football.Team;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import com.domain.logic.users.SystemManagerMember;
import com.domain.logic.utils.SHA1Function;

import java.util.ArrayList;

public class SystemManagerAcceptanceTest {
    private DomainController serviceLayerManager;

    public static void activate(String UC_NAME) {
        DomainController serviceLayerManager = new DomainController(DBCommunicator.getInstance());
        try {
            serviceLayerManager.performResetSystem();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        //perform test on the given US:S
        switch (UC_NAME) {
            case "1.1": {
                //test on US delete member from the system
                testUsRemoveMember(serviceLayerManager);
                break;
            }
            case "1.2": {
                break;
            }
            case "1.3": {
                break;
            }
            case "1.4": {
                break;
            }
            case "1.5": {
                break;
            }

        }
    }

    private static void testUsRemoveMember(DomainController serviceLayerManager) {
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
        SystemManagerMember newSysMan = new SystemManagerMember("korenTest", "bip", "sysEmail",
                new Address("a", "b", "c", "d"), "mr.Manager");
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
                }
                else {
                    System.err.println("team owner " + teamOwner.getMember().getName() +" was deleted. he is the only owner of his team so he can't be deleted" +
                            "!" );
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
