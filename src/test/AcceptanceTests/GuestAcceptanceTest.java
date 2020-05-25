import com.data.DBCommunicator;
import com.domain.domaincontroller.DomainController;
import com.domain.logic.users.Guest;
import com.domain.logic.utils.SHA1Function;

public class GuestAcceptanceTest {

    public GuestAcceptanceTest() {
    }

    public static void activate(String UC_NAME){
        DomainController serviceLayerManager = new DomainController(DBCommunicator.getInstance());
        Guest guest = new Guest("unknown","unknown");

        switch (UC_NAME) {

            case ("3.1"): {
                serviceLayerManager.performRegisterAsMember(guest,"User400","123","@","US","Cali","","1234");
               if (serviceLayerManager.findMember("User400", SHA1Function.hash("123")) == null) {
                   System.err.println("user registration failed");
               }
                break;
            }

            case ("3.2"): {
                serviceLayerManager.performRegisterAsMember(guest,"User400","123","@","US","Cali","","1234");
                serviceLayerManager.performLogin(guest,"User400","123");
                break;
            }

            case ("3.3"): {
                serviceLayerManager.performRegisterAsMember(guest,"User400","123","@","US","Cali","","1234");
                serviceLayerManager.performSearch(guest,1,"User400");
                break;
            }

            default: {
                System.out.println("Wrong guest UC code.");
            }

        }
    }
}
