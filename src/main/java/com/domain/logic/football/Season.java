package com.domain.logic.football;


import com.domain.logic.policies.Policy;

import java.util.ArrayList;
import java.util.HashMap;

public class Season {

    private int year;
    private ArrayList<SeasonInLeague> seasonInLeagues;


    public Season(int year) {
        this.year = year;
        this.seasonInLeagues = new ArrayList<>();
        //TODO call DAO to add new Season
    }

    private SeasonInLeague findSeasonInLeague(League league){
        for (SeasonInLeague seasonInLeague : this.seasonInLeagues){
            if (seasonInLeague.getLeague().getLeagueName().equals(league.getLeagueName()))
                return seasonInLeague;
        }
        return null;
    }

    public void addGame(Game game){
        SeasonInLeague seasonInLeague = findSeasonInLeague(game.getLeague());
        seasonInLeague.addGame(game);
    }

    public void addSeasonInLeague(SeasonInLeague seasonInLeague){
        this.seasonInLeagues.add(seasonInLeague);
    }

    public int getYear() {
        return this.year;
    }

    public void setPolicyToLeague(League league, Policy policy){
        SeasonInLeague seasonInLeague = findSeasonInLeague(league);
        seasonInLeague.setPolicy(policy);
    }

    public ArrayList<League> getLeagues() {
        ArrayList<League> result = new ArrayList<>();
        for (SeasonInLeague seasonInLeague : this.seasonInLeagues)
            result.add(seasonInLeague.getLeague());
        return result;
    }

    public HashMap<League, Policy> getSeasonLeaguePolicy() {
        HashMap<League, Policy> result = new HashMap<>();
        for (SeasonInLeague seasonInLeague : this.seasonInLeagues)
            result.put(seasonInLeague.getLeague(), seasonInLeague.getPolicy());
        return result;
    }

    public ArrayList<Game> getGames() {
        ArrayList<Game> result = new ArrayList<>();
        for (SeasonInLeague seasonInLeague : this.seasonInLeagues)
            result.addAll(seasonInLeague.getGames());
        return result;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
