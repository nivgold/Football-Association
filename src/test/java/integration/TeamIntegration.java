package integration;

import com.domain.logic.AssociationSystem;
import com.domain.logic.enums.TeamStatus;
import com.domain.logic.football.*;
import com.domain.logic.roles.Coach;
import com.domain.logic.roles.Player;
import com.domain.logic.roles.TeamOwner;
import com.domain.logic.users.Member;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class TeamIntegration {

    Field field = new Field("Israel", "hasharon", "Herzlya", "123");
    League league = new League("test league");
    Member member1 = new Member("Talfrim", "123", "x@x.x", null, "Tal");
    Member member2 = new Member("member2", "123", "x@x.x", null, "Tal");
    TeamOwner teamOwner1;

    {
        try {
            teamOwner1 = new TeamOwner(member1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Field filed1 = new Field("Israel", "hasharon", "Herzliya", "12345");
    Field filed2 = new Field("Israel", "hasharon", "Herzliya", "54321");
    Team team1 = new Team("team1-test", TeamStatus.Open, teamOwner1, filed1);
    Team team2 = new Team("team2-test", TeamStatus.Open, teamOwner1, filed2);
    SeasonInLeague seasonInLeague = new SeasonInLeague(league, new Season(2019));
    Game game = new Game(team1, team2, seasonInLeague, LocalDateTime.now(), filed1);

    @Test
    void testRemoveTeamPermanently() {
        AssociationSystem.getInstance().clearSystem();
        ArrayList<PlayerRoleInTeam> playersInTeam = team1.getPlayers();
        ArrayList<Player> players = new ArrayList<>();
        for (PlayerRoleInTeam p : playersInTeam) {
            players.add(p.getPlayer());
        }

        ArrayList<CoachInTeam> coachesInTeam = team1.getCoachesInTeam();
        ArrayList<Coach> coaches = new ArrayList<>();
        for (CoachInTeam coachInTeam : coachesInTeam) {
            coaches.add(coachInTeam.getCoach());
        }


        //ArrayList<TeamOwner> teamOwners = team1.getTeam_owners();
        //ArrayList<TeamManager> teamManagers = team1.getTeam_managers();
        //ArrayList<Member> members = new ArrayList<>();
        //for (TeamOwner teamOwner : teamOwners) {
        //    members.add(teamOwner.getMember());
        //}
        //for (TeamManager teamManager : teamManagers) {
        //    members.add(teamManager.getMember());
        //}
        try {
            team1.removeTeamPermanently();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(Player p : players) {
            assertFalse(playerConnectedToTeam(p,team1));
        }
        //TODO same for coaches
    }

    private boolean playerConnectedToTeam(Player p, Team team1) {
        for (PlayerRoleInTeam r : p.getRoleInTeams()) {
            if (r.getTeam()==team1)
                return true;
        }
        return false;
    }
}