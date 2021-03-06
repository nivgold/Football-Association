import com.data.DBCommunicator;
import com.domain.domaincontroller.DomainController;
import com.domain.logic.enums.EventType;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.*;
import com.domain.logic.roles.Referee;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class RefereeAcceptanceTest {

    public RefereeAcceptanceTest() {
    }

    public static void activate(String UC_NAME){
        DomainController serviceLayerManager = new DomainController(DBCommunicator.getInstance());
        serviceLayerManager.login("sysManager_admin", "admin", "", "");
        try {
            serviceLayerManager.performResetSystem("sysManager_admin");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
//        Referee referee = null;
//        try {
//            referee = new Referee(new Member("","","",null,""));
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//        Team team1 = null;
//        try {
//            team1 = new Team("test Team 1", TeamStatus.Open, new TeamOwner(new Member("","","",null,"")), new Field("","",",",""));
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//        Team team2 = null;
//        try {
//            team2 = new Team("test Team 2", TeamStatus.Open, new TeamOwner(new Member("","","",null,"")), new Field("","",",",""));
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//        SeasonInLeague seasonInLeague = new SeasonInLeague(new League(""), new Season(2019));
//        Game game1 = new Game(team1, team2, seasonInLeague, LocalDateTime.now(), new Field("","",",",""));
//        Game game2 = new Game(team2, team1, seasonInLeague, LocalDateTime.now(), new Field("","",",",""));
//        referee.addMainGame(game1);
//        referee.addSideGame(game2);
        switch (UC_NAME) {

            case ("6.2"): {
//                ArrayList<Game> gamesToChooseFrom = serviceLayerManager.getGamesToView(referee);
//                if (!(gamesToChooseFrom.size()==2) || !gamesToChooseFrom.contains(game1) || !gamesToChooseFrom.contains(game2)) {
//                    System.err.println("wrong referee games to view list");
//                }
//                Game chosenGame = game1;
//                String output = serviceLayerManager.viewGame(chosenGame);
//                if (!output.equals(game1.toString())) {
//                    System.err.println("wrong game displayed to referee");
//                }
                break;
            }

            case ("6.3"): {
                // referee create game event
                String refereeUsername = "main Referee asaf";
                serviceLayerManager.login(refereeUsername, "asafpass", "", "");
                int gameID = 1;
                int gameMinute = 20;
                String description = "some description";
                String type = "YellowCard";
                String playerUsername = "teamOwner_Niv";

                serviceLayerManager.addGameEvent(refereeUsername, gameID, gameMinute, description, type, playerUsername);
            }

            case ("6.4"): {
                String refereeUsername = "main Referee asaf";
                serviceLayerManager.login(refereeUsername, "asafpass", "123", "123");
                int gameID = 1;
                String report = "bla bla bla";

                serviceLayerManager.createGameReport(refereeUsername, gameID, report);
                break;
            }

            default:
                System.out.println("Oops... wrong UC code for referee.");
        }

    }
}
