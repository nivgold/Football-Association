package com.domain.logic;

import com.domain.logic.enums.SearchCategory;
import com.domain.logic.football.League;
import com.domain.logic.football.Season;
import com.domain.logic.football.Team;
import com.domain.logic.roles.Coach;
import com.domain.logic.roles.Player;
import com.domain.logic.users.Member;

import java.util.*;

public class SearchSystem {
    private static SearchSystem searchSystem = new SearchSystem();

    public static SearchSystem getInstance() {
        return searchSystem;
    }

    private SearchSystem() {
    }

    /**
     * letting the users of the system to search by member's name
     * @param name - the logic's name search
     */
    private String searchByName(String name){
        ArrayList<String> result = new ArrayList<>();
        //TODO call DAO find members with name
        ManageMembers manageMembersSystem = ManageMembers.getInstance();
        ArrayList<Member> members = manageMembersSystem.findMemberByName(name);
        return members.toString();
    }

    /**
     * letting the users of the system to search by categories
     * @param category - the logic's category search
     */
    private String searchByCategory(SearchCategory category){
        if (category == SearchCategory.Coach){
            // search all coaches
            //TODO call DAO to return all Coaches from the DB
            ArrayList<Coach> coaches = ManageMembers.getInstance().findAllCoaches();
            return coaches.toString();
        }
        else if (category == SearchCategory.Player){
            // search all players
            //TODO call DAO to return all Players from the DB
            ArrayList<Player> players = ManageMembers.getInstance().findAllPlayers();
            return players.toString();
        }
        else{
            // search all teams
            //TODO call DAO to return all Teams from the DB
            return Arrays.toString(ManageTeams.getInstance().getAllTeams().toArray());
        }
    }

    /**
     * letting the users of the system to search by keywords
     * @param keywords - the logic's keywords search
     */
    private String searchByKeywords(String keywords){
        // searching by members name, userName, teamName, seasonYear, leagueName
        ArrayList<String> searchResult = new ArrayList<>();
        for (Member member : ManageMembers.getInstance().findMemberByUsername(keywords))
            searchResult.add(member.getUserName());
        for (Member member : ManageMembers.getInstance().findMemberByName(keywords))
            searchResult.add(member.getName());
        for (Team team : ManageTeams.getInstance().findTeamByName(keywords))
            searchResult.add(team.getTeamName());
        for (Season season : ManageSeasons.getInstance().findSeasonByYear(keywords))
            searchResult.add(season.getYear()+"");
        for (League league : ManageLeagues.getInstance().findLeagueByName(keywords))
            searchResult.add(league.getLeagueName());

        return searchResult.toString();
    }

    /**
     * showing the search history of the given member
     * @param member - the system member whose search history is going to be shown
     */
    public void watchSearchHistory(Member member){

    }

    public String search(int key, String query) {
        if(key == 1){
            return searchByName(query);
        }
        else if(key == 2){
            return searchByCategory(SearchCategory.valueOf(query));
        }
        else if(key == 3){
            return searchByKeywords(query);
        }
        else{
            return "invalid key";
        }
    }
}
