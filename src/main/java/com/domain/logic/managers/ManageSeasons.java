package com.domain.logic.managers;

import com.domain.logic.football.Season;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageSeasons {
    // static variable single_instance of type Singleton
    private static ManageSeasons manageSeasons = null;

    private HashMap<Integer, Season> seasonsHashMap;

    // private constructor restricted to this class itself
    private ManageSeasons()
    {
        seasonsHashMap = new HashMap<>();
    }


    // static method to create instance of Singleton class
    public static ManageSeasons getInstance()
    {
        if (manageSeasons == null)
            manageSeasons = new ManageSeasons();
        return manageSeasons;
    }


    /**
     * @param seasonYear
     * @return the season in that year
     */
    public Season findSeason(Integer seasonYear){
        return seasonsHashMap.get(seasonYear);
    }


    /**
     * removes season from data
     * @param seasonYear
     * @return boolean ans if successful
     */
    public boolean removeSeason(Integer seasonYear){
        Season removed = seasonsHashMap.remove(seasonYear);
        if(removed != null)
            return true;
        return false;
    }


    /**
     * @param season
     * @return a boolean answer that represent if it possible to add the given season.
     * if true it adds the season to the season hash.
     */
    public boolean addSeason(Season season){
        if(seasonsHashMap.containsKey(season.getYear())){
            return false;
        }
        seasonsHashMap.put(season.getYear(), season);
        return true;
    }


    /**
     * method that helps the search engine.
     * @param keywords
     * @return all seasons that has those keywords
     */
    public ArrayList<Season> findSeasonByYear(String keywords) {
        ArrayList<Season> seasons = new ArrayList<>();
        for (Season season : this.seasonsHashMap.values()){
            if (("" + season.getYear()).equals(keywords))
                seasons.add(season);
        }
        return seasons;
    }

    public void removeAllSeasons() {
        this.seasonsHashMap = new HashMap<>();
    }
}
