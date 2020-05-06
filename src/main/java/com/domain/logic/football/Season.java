package com.domain.logic.football;


import com.domain.domaincontroller.managers.ManageSeasons;
import com.domain.logic.policies.Policy;

import java.util.ArrayList;
import java.util.HashMap;

public class Season {

    private int year;
    private ArrayList<League> leagues;
    private HashMap<League, Policy> seasonLeaguePolicy;
    private ArrayList<Game> games;

    public Season(int year) {
        this.year = year;
        this.seasonLeaguePolicy = new HashMap<>();
        this.games = new ArrayList<>();
        leagues = new ArrayList<>();
        ManageSeasons.getInstance().addSeason(this);
    }

    public void addGame(Game game){
        this.games.add(game);
    }

    public void addLeague(League league) {
        leagues.add(league);
    }

    public int getYear() {
        return this.year;
    }

    public void setPolicyToLeague(League league, Policy policy){
        this.seasonLeaguePolicy.put(league, policy);
    }

    public ArrayList<League> getLeagues() {
        return leagues;
    }

    public HashMap<League, Policy> getSeasonLeaguePolicy() {
        return seasonLeaguePolicy;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setLeagues(ArrayList<League> leagues) {
        this.leagues = leagues;
    }

    public void setSeasonLeaguePolicy(HashMap<League, Policy> seasonLeaguePolicy) {
        this.seasonLeaguePolicy = seasonLeaguePolicy;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }
}
