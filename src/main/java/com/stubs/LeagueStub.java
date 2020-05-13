package com.stubs;

import com.domain.logic.football.League;
import com.domain.logic.football.Season;
import com.domain.logic.football.SeasonInLeague;
import com.domain.logic.policies.GameSettingPolicy;
import com.domain.logic.policies.Policy;
import com.domain.logic.policies.RankingPolicy;
import com.domain.logic.roles.Referee;

import java.util.ArrayList;
import java.util.HashMap;

public class LeagueStub extends League {

    private ArrayList<Season> seasons;
    private HashMap<Season, ArrayList<Referee>> leagueRefereeMap;
    private HashMap<Season, Policy> seasonLeaguePolicy;

    public LeagueStub(String leagueName) {
        super(leagueName);
        this.seasons = new ArrayList<>();
        Season season = new Season(2012);
        SeasonInLeague seasonInLeague = new SeasonInLeague(this, season);
        this.seasons.add(season);
        this.leagueRefereeMap = new HashMap<>();
        this.seasonLeaguePolicy = new HashMap<>();
        Policy policy = new Policy(seasonInLeague, null, null);
        this.seasonLeaguePolicy.put(season, policy);
        //GameSettingPolicy gameSettingPolicy = new GameSettingPolicy(policy, new OneMatchEachPairSettingPolicy());
        //policy.setGameSettingPolicy(gameSettingPolicy);
        //RankingPolicy rankingPolicy = new RankingPolicy(policy, 5, 1, 0, 0, 0);
        //policy.setRankingPolicy(rankingPolicy);
    }

    @Override
    public ArrayList<Season> getSeasons() {
        return this.seasons;
    }

    @Override
    public void addSeason(Season season) {
        this.seasons.add(season);
    }

    @Override
    public HashMap<Season, ArrayList<Referee>> getLeagueRefereeMap() {
        return this.leagueRefereeMap;
    }

    @Override
    public HashMap<Season, Policy> getSeasonLeaguePolicy() {
        return this.seasonLeaguePolicy;
    }

    @Override
    public void setRankingPolicy(Season season, RankingPolicy rPolicy) {
        this.seasonLeaguePolicy.get(season).setRankingPolicy(rPolicy);
    }

    @Override
    public void setGameSettingPolicy(Season season, GameSettingPolicy settingPolicy) {
        this.seasonLeaguePolicy.get(season).setGameSettingPolicy(settingPolicy);
    }
}
