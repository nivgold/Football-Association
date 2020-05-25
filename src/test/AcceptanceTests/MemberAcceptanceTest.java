import com.data.DBCommunicator;
import com.domain.domaincontroller.DomainController;

public class MemberAcceptanceTest {

    public MemberAcceptanceTest() {
    }

    public static void activate(String UC_NAME){
        DomainController serviceLayerManager = new DomainController(DBCommunicator.getInstance());
        try {
            serviceLayerManager.performResetSystem();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        switch (UC_NAME){

        }
    }
}
