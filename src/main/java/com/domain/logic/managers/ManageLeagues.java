package com.domain.logic.managers;

import com.domain.logic.football.League;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageLeagues {
    // static variable single_instance of type Singleton
    private static ManageLeagues manageLeagues = null;

    private HashMap<String, League> leaguesHashMap;

    // private constructor restricted to this class itself
    private ManageLeagues()
    {
        leaguesHashMap = new HashMap<>();
    }


    // static method to create instance of Singleton class
    public static ManageLeagues getInstance()
    {
        if (manageLeagues == null)
            manageLeagues = new ManageLeagues();
        return manageLeagues;
    }


    /**
     * @param leagueName
     * @return the league with that name
     */
    public League findLeague(String leagueName){
        return leaguesHashMap.get(leagueName);
    }


    /**
     * removes league from data
     * @param leagueName
     * @return boolean ans if successful
     */
    public boolean removeLeague(String leagueName){
        League removed = leaguesHashMap.remove(leagueName);
        if(removed != null)
            return true;
        return false;
    }


    /**
     * @param league
     * @return a boolean answer that represent if it possible to add the given league.
     * if true it adds the league to the leagues hash.
     */
    public boolean addLeague(League league){
        if(leaguesHashMap.containsKey(league.getLeagueName())){
            return false;
        }
        leaguesHashMap.put(league.getLeagueName(), league);
        return true;
    }


    /**
     * method that helps the search engine.
     * @param keywords
     * @return all leagues that has those keywords
     */
    public ArrayList<League> findLeagueByName(String keywords) {
        ArrayList<League> leagues = new ArrayList<>();
        for (League league : leaguesHashMap.values()){
            if (league.getLeagueName().equalsIgnoreCase(keywords))
                leagues.add(league);
        }
        return leagues;
    }


    /**
     * @return arrayList of all leagues in the system
     */
    public ArrayList<League> getAllLeagues(){
        ArrayList<League> allLeagues = new ArrayList<>();
        for(League league : this.leaguesHashMap.values()){
            allLeagues.add(league);
        }
        return allLeagues;
    }

    public void removeAllLeagues() {
        this.leaguesHashMap = new HashMap<>();
    }
}
