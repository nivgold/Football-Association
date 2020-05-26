import com.data.DBCommunicator;
import com.domain.domaincontroller.DomainController;

public class MemberAcceptanceTest {

    public MemberAcceptanceTest() {
    }

    public static void activate(String UC_NAME){
        DomainController serviceLayerManager = new DomainController(DBCommunicator.getInstance());
        serviceLayerManager.login("sysManager_admin", "admin", "", "");
        try {
            serviceLayerManager.performResetSystem("sysManager_admin");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        switch (UC_NAME){

        }
    }
}
