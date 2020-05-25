import com.data.DBCommunicator;
import com.domain.domaincontroller.DomainController;
import com.domain.logic.roles.TeamManager;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Guest;
import com.domain.logic.users.Member;

import java.util.ArrayList;

public class TeamOwnerAcceptanceTest {
    private static String nivTeamName = "NIV Team";
    public TeamOwnerAcceptanceTest() {
    }

    public static void activate(String UC_NAME){
        DomainController serviceLayerManager = new DomainController(DBCommunicator.getInstance());
        try {
            serviceLayerManager.performResetSystem();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        switch (UC_NAME){
            case "4.1"://create team
            {
                Guest guest = new Guest("teamOwner", "Koren");
                Member koren;
                try {
                    koren = serviceLayerManager.login("teamOwner_Koren", "korenpass", "koren", "");
                    serviceLayerManager.createTeam(koren.getUserName(), "KOREN Team", "Israel", "None", "Tel Aviv", "7346574236");
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                break;
            }
            case "4.2":
            {
                TeamOwner teamOwner = serviceLayerManager.findAllTeamOwner(nivTeamName).get(0);
                Guest guestTemp = new Guest("new", "ownerToNiv");
                Member member = guestTemp.registerAsMember("new ownerToNiv", "ownerToNivpass", "owner2@gmail.com","Israel" , "Israel", "Tel Aviv", "8109054");
                System.out.println("owner1 is the main team owner of the team \"team1\"");
                try {
                    serviceLayerManager.performAppointTeamOwner(teamOwner, member);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                System.out.println("owner2 is a team owner in team \"Niv Team\" by owner1");
                // remove the team owner
                System.out.println("owner1 wish to remove owner2 from being team owner in team \"team1\"");
                try {
                    serviceLayerManager.performRemoveTeamOwner(teamOwner, (TeamOwner)member.getRoles().get(0));
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                serviceLayerManager.logout();
                try {
                    member = serviceLayerManager.login("new ownerToNiv", "ownerToNivpass", "", "");
                    ArrayList<TeamOwner> allTeamOwners = serviceLayerManager.findAllTeamOwner(nivTeamName);
                    if (allTeamOwners.size() != 1){
                        System.err.println("The Remove Team Owner UC was NOT performed successfully");
                        return;
                    }
                    try {
                        if (member.getSpecificRole(TeamOwner.class) != null){
                            System.err.println("The Remove Team Owner UC was NOT performed successfully");
                            return;
                        }
                    } catch (ClassNotFoundException e) {
                        System.out.println("owner2 was successfully removed from being team owner in team \"team1\"");
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                break;
            }
            case "4.3":
            {
                TeamOwner teamOwner = serviceLayerManager.findAllTeamOwner(nivTeamName).get(0);
                System.out.println("owner1 is the main team owner of the team \"team1\"");
                // appoint new team owner
                Guest guestTemp = new Guest("new", "ownerToNiv");
                Member member = guestTemp.registerAsMember("new ownerToNiv", "ownerToNivpass", "owner2@gmail.com","Israel" , "Israel", "Tel Aviv", "8109054");
                System.out.println("owner1 wish to appoint owner2 to be a team manager in team \"team1\"");
                try {
                    serviceLayerManager.performAppointTeamManager(teamOwner, member);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                serviceLayerManager.logout();
                try {
                    member = serviceLayerManager.login("new ownerToNiv", "ownerToNivpass", "", "");
                    ArrayList<TeamManager> allTeamManagers= serviceLayerManager.findAllTeamManager(nivTeamName);
                    if (allTeamManagers.size() == 0){
                        System.err.println("The Appoint New Team Owner UC was NOT performed successfully");
                        return;
                    }
                    if (member.getSpecificRole(TeamManager.class) == null) {
                        System.err.println("The Appoint New Team Owner UC was NOT performed successfully");
                        return;
                    }
                    System.out.println("owner2 is now a team manager in team \"NIV Team\"");
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                break;
            }
            case "4.4":
            {
                TeamOwner teamOwner = serviceLayerManager.findAllTeamOwner(nivTeamName).get(0);
                Guest guestTemp = new Guest("new", "ownerToNiv");
                Member member = guestTemp.registerAsMember("new ownerToNiv", "ownerToNivpass", "owner2@gmail.com","Israel" , "Israel", "Tel Aviv", "8109054");
                System.out.println("owner1 is the main team owner of the team \"team1\"");
                try {
                    serviceLayerManager.performAppointTeamManager(teamOwner, member);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                System.out.println("owner2 is a team manager in team \"team2\" by owner1");
                // remove the team owner
                System.out.println("owner1 wish to remove owner2 from being team owner in team \"team1\"");
                try {
                    serviceLayerManager.performRemoveTeamOwner(teamOwner, (TeamOwner)member.getRoles().get(0));
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                try {
                    serviceLayerManager.logout();
                    member = serviceLayerManager.login("new ownerToNiv", "ownerToNivpass", "", "");
                    ArrayList<TeamOwner> allTeamOwners = serviceLayerManager.findAllTeamOwner(nivTeamName);
                    if (allTeamOwners.size() != 1){
                        System.err.println("The Remove Team Owner UC was NOT performed successfully");
                        return;
                    }
                    try {
                        if (member.getSpecificRole(TeamOwner.class) != null){
                            System.err.println("The Remove Team Owner UC was NOT performed successfully");
                            return;
                        }
                    } catch (ClassNotFoundException e) {
                        System.out.println("owner2 was successfully removed from being team owner in team \"NIV Team\"");
                    }
                    break;
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            case "4.5":
            {
                break;
            }
            case "4.6":
            {
                break;
            }
            case "4.7":
            {
                break;
            }
            case "4.72":
            {
                break;
            }
            case "4.8":
            {
                break;
            }
            case "4.82":
            {
                break;
            }
            case "4.9":
            {
                break;
            }
        }
    }
}
