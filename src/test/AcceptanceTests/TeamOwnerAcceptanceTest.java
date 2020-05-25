import com.data.DBCommunicator;
import com.domain.domaincontroller.DomainController;
import com.domain.logic.data_types.Address;
import com.domain.logic.roles.TeamManager;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import com.domain.logic.utils.SHA1Function;

import static com.oracle.jrockit.jfr.ContentType.Address;

public class TeamOwnerAcceptanceTest {
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
            case "4.1":
            {
                TeamOwner teamOwner = serviceLayerManager.findAllTeamOwner().get(0);
                System.out.println("owner1 is the main team owner of the team \"team1\"");
                // appoint new team owner
                Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
                System.out.println("owner1 wish to appoint owner2 to be also team owner in team \"team1\"");
                try {
                    serviceLayerManager.performAppointTeamOwner(teamOwner, member);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                if (member.getRoles().size()==0){
                    System.err.println("The Appoint New Team Owner UC was NOT performed successfully");
                    return;
                }
                if (member.getRoles().get(0).getClass() != TeamOwner.class){
                    System.err.println("The Appoint New Team Owner UC was NOT performed successfully");
                    return;
                }
                TeamOwner owner2 = (TeamOwner) member.getRoles().get(0);
                if (owner2.getTeam()!=teamOwner.getTeam()){
                    System.err.println("The Appoint New Team Owner UC was NOT performed successfully");
                    return;
                }
                if (owner2.getAppointer() != teamOwner){
                    System.err.println("The Appoint New Team Owner UC was NOT performed successfully");
                    return;
                }
                System.out.println("owner2 is now a team owner in team \"team1\"");
                break;
            }
            case "4.2":
            {
                TeamOwner teamOwner = serviceLayerManager.findAllTeamOwner().get(0);
                Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
                System.out.println("owner1 is the main team owner of the team \"team1\"");
                try {
                    serviceLayerManager.performAppointTeamOwner(teamOwner, member);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                System.out.println("owner2 is a team owner in team \"team2\" by owner1");

                // remove the team owner
                System.out.println("owner1 wish to remove owner2 from being team owner in team \"team1\"");
                try {
                    serviceLayerManager.performRemoveTeamOwner(teamOwner, (TeamOwner)member.getRoles().get(0));
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                if (teamOwner.getTeam().getTeam_owners().size()!=1){
                    System.err.println("The Remove Team Owner UC was NOT performed successfully");
                    return;
                }
                if (member.getRoles().size()!=0){
                    System.err.println("The Remove Team Owner UC was NOT performed successfully");
                    return;
                }
                if (teamOwner.getAppointments().size()!=0){
                    System.err.println("The Remove Team Owner UC was NOT performed successfully");
                    return;
                }
                System.out.println("owner2 was successfully removed from being team owner in team \"team1\"");
                break;
            }
            case "4.3":
            {
                TeamOwner teamOwner = serviceLayerManager.findAllTeamOwner().get(0);
                System.out.println("owner1 is the main team owner of the team \"team1\"");
                // appoint new team owner
                Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
                System.out.println("owner1 wish to appoint owner2 to be a team manager in team \"team1\"");
                try {
                    serviceLayerManager.performAppointTeamManager(teamOwner, member);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (member.getRoles().size()==0){
                    System.err.println("The Appoint New Team Owner UC was NOT performed successfully");
                    return;
                }
                if (member.getRoles().get(0).getClass() != TeamManager.class){
                    System.err.println("The Appoint New Team Owner UC was NOT performed successfully");
                    return;
                }
                TeamManager owner2 = (TeamManager) member.getRoles().get(0);
                if (owner2.getTeam()!=teamOwner.getTeam()){
                    System.err.println("The Appoint New Team Owner UC was NOT performed successfully");
                    return;
                }
                if (owner2.getAppointer() != teamOwner){
                    System.err.println("The Appoint New Team Owner UC was NOT performed successfully");
                    return;
                }
                System.out.println("owner2 is now a team manager in team \"team1\"");
                break;
            }
            case "4.4":
            {
                TeamOwner teamOwner = serviceLayerManager.findAllTeamOwner().get(0);
                Member member = new Member("owner2", SHA1Function.hash("owner2"), "owner2@gmail.com", new Address("Israel" , "Israel", "Tel Aviv", "8109054"), "shimon");
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
                if (teamOwner.getTeam().getTeam_owners().size()!=1){
                    System.err.println("The Remove Team Owner UC was NOT performed successfully");
                    return;
                }
                if (member.getRoles().size()!=0){
                    System.err.println("The Remove Team Owner UC was NOT performed successfully");
                    return;
                }
                if (teamOwner.getAppointments().size()!=0){
                    System.err.println("The Remove Team Owner UC was NOT performed successfully");
                    return;
                }
                System.out.println("owner2 was successfully removed from being team owner in team \"team1\"");
                break;
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
