package com.domain.logic.managers;

import com.domain.logic.football.Team;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageTeams {
    // static variable single_instance of type Singleton
    private static ManageTeams manageTeams = null;

    private HashMap<String, Team> TeamsHashMap;

    // private constructor restricted to this class itself
    private ManageTeams()
    {
        TeamsHashMap = new HashMap<>();
    }


    // static method to create instance of Singleton class
    public static ManageTeams getInstance()
    {
        if (manageTeams == null)
            manageTeams = new ManageTeams();
        return manageTeams;
    }


    /**
     * @param TeamName
     * @return the Business.football.Team with that name
     */
    public Team findTeam(String TeamName){
        return TeamsHashMap.get(TeamName);
    }


    public boolean removeTeam(String TeamName){
        Team removed = TeamsHashMap.remove(TeamName);
        if(removed != null)
            return true;
        return false;
    }


    /**
     * @param Team
     * @return a boolean answer that represent if it possible to add the given Business.football.Team.
     * if true it adds the team to the teams hash.
     */
    public boolean addTeam(Team Team){
        if(TeamsHashMap.containsKey(Team.getTeamName())){
            return false;
        }
        TeamsHashMap.put(Team.getTeamName(), Team);
        return true;
    }


    /**
     * method that helps the search engine.
     * @param keywords
     * @return all Teams that has those keywords
     */
    public ArrayList<Team> findTeamByName(String keywords) {
        ArrayList<Team> teams = new ArrayList<>();
        for (Team Team : TeamsHashMap.values()){
            if (Team.getTeamName().equalsIgnoreCase(keywords))
                teams.add(Team);
        }
        return teams;
    }


    public ArrayList<Team> getAllTeams() {
        ArrayList<Team> teams = new ArrayList<>();
        for(Team team: TeamsHashMap.values()){
            teams.add(team);
        }
        return teams;
    }

    public void removeAllTeams() {
        this.TeamsHashMap = new HashMap<>();
    }
}
