import com.data.DBCommunicator;
import com.domain.domaincontroller.DomainController;
import com.domain.logic.users.SystemManagerMember;
import com.domain.logic.utils.SHA1Function;

import java.util.ArrayList;

public class SystemAcceptanceTest {

    public SystemAcceptanceTest(){
    }

    public static void activate(String UC_NAME){
        // reset system UC
        if (UC_NAME == "0.1"){
            DomainController serviceLayerManager = new DomainController(DBCommunicator.getInstance());
            try {
                serviceLayerManager.performResetSystem();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            // tests
            ArrayList<SystemManagerMember> systemManagerMembers = serviceLayerManager.findAllSystemManagers();
            if (systemManagerMembers.size()!=1){
                System.err.println("The System Reset UC was NOT successfully performed");
                return;
            }

            SystemManagerMember systemManagerMember = systemManagerMembers.get(0);
            if (!systemManagerMember.getUserName().equals("managerSys") || !systemManagerMember.getPasswordHash().equals(SHA1Function.hash("admin"))){
                System.err.println("The System Reset UC was NOT successfully performed");
                return;
            }

            if (serviceLayerManager.findMember("ref1", SHA1Function.hash("123")) == null){
                System.err.println("The System Reset UC was NOT successfully performed");
                return;
            }
            if (serviceLayerManager.findMember("ref2", SHA1Function.hash("456")) == null){
                System.err.println("The System Reset UC was NOT successfully performed");
                return;
            }
            if (serviceLayerManager.findMember("ref3", SHA1Function.hash("789")) == null){
                System.err.println("The System Reset UC was NOT successfully performed");
                return;
            }
            if (serviceLayerManager.findMember("ass", SHA1Function.hash("a123")) == null){
                System.err.println("The System Reset UC was NOT successfully performed");
                return;
            }
            if (serviceLayerManager.findMember("owner1", SHA1Function.hash("owner1")) == null){
                System.err.println("The System Reset UC was NOT successfully performed");
                return;
            }
            if (serviceLayerManager.findMember("owner2", SHA1Function.hash("owner2")) == null){
                System.err.println("The System Reset UC was NOT successfully performed");
                return;
            }
            
            if(serviceLayerManager.findTeam("team1") == null){
                System.err.println("The System Reset UC was NOT successfully performed");
                return;
            }
            if(serviceLayerManager.findTeam("team2") == null){
                System.err.println("The System Reset UC was NOT successfully performed");
                return;
            }
            System.out.println("The System Reset UC was successfully performed");
        }
    }
}
