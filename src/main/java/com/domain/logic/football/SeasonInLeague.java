package com.domain.logic.football;

import com.domain.logic.policies.Policy;
import com.domain.logic.roles.Referee;

import java.util.ArrayList;

public class SeasonInLeague {
    private League league;
    private Season season;
    private Policy policy;
    private ArrayList<Team> teams;
    private ArrayList<Referee> referees;
    private ArrayList<Game> games;

    public SeasonInLeague(League league, Season season) {
        this.league = league;
        this.season = season;
        this.teams = new ArrayList<>();
        this.referees = new ArrayList<>();
        this.games = new ArrayList<>();

        league.addSeasonInLeague(this);
        season.addSeasonInLeague(this);
    }

    public void setPolicy(Policy policy){
        this.policy = policy;
    }

    public void addTeam(Team team){
        this.teams.add(team);
    }

    public void addGame(Game game){
        this.games.add(game);
    }

    public void addReferee(Referee referee){
        this.referees.add(referee);
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    public League getLeague() {
        return league;
    }

    public Season getSeason() {
        return season;
    }

    public Policy getPolicy() {
        return policy;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public ArrayList<Referee> getReferees() {
        return referees;
    }
}
