package com.stubs;

import com.data.DBCommunicator;
import com.data.Dao;
import com.domain.logic.football.League;
import com.domain.logic.football.Season;
import com.domain.logic.football.SeasonInLeague;
import com.domain.logic.policies.GameSettingPolicy;
import com.domain.logic.policies.Policy;
import com.domain.logic.policies.RankingPolicy;
import com.domain.logic.policies.game_setting_policies.OneMatchEachPairSettingPolicy;
import com.domain.logic.roles.Referee;
import com.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class LeagueStub extends League {

    private ArrayList<Season> seasons;
    private HashMap<Season, ArrayList<Referee>> leagueRefereeMap;
    private HashMap<Season, Policy> seasonLeaguePolicy;
    private String leagueName;
    public ArrayList<SeasonInLeague> seasonInLeagues;

    public LeagueStub(String leagueName) {
        super(leagueName);
        this.seasonInLeagues = new ArrayList<>();
        this.leagueName = leagueName;
        this.seasons = new ArrayList<>();
        Season season = new Season(2012);
        SeasonInLeague seasonInLeague = new SeasonInLeague(this, season);
        this.seasonInLeagues.add(seasonInLeague);
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
        HashMap<Season, Policy> hashMap = new HashMap<>();
        for (SeasonInLeague seasonInLeague : this.seasonInLeagues){
            hashMap.put(seasonInLeague.getSeason(), seasonInLeague.getPolicy());
        }
        return hashMap;
    }

    @Override
    public void setRankingPolicy(Season season, RankingPolicy rPolicy) throws Exception {
        SeasonInLeague seasonInLeague = findSeasonInLeague(season);
        if (seasonInLeague == null)
            return;

        Policy policy = seasonInLeague.getPolicy();
        policy.setRankingPolicy(rPolicy);

        Dao dao = DBStub.getInstance();
        dao.setGameRankingPolicy(season.getYear(), this.leagueName, rPolicy.getWin(), rPolicy.getGoals(), rPolicy.getDraw(), rPolicy.getYellowCards(), rPolicy.getRedCards());
        //com.logger
        Logger logger = Logger.getInstance();
        logger.saveLog("Ranking Business.policies.Policy assigned to season " + season.getYear() + " in league " + leagueName);
    }

    @Override
    public SeasonInLeague findSeasonInLeague(Season season){
        for (SeasonInLeague seasonInLeague : this.seasonInLeagues){
            if (seasonInLeague.getSeason().getYear()==season.getYear())
                return seasonInLeague;
        }
        return null;
    }

    @Override
    public void setGameSettingPolicy(Season season, GameSettingPolicy settingPolicy) throws Exception {
        SeasonInLeague seasonInLeague = findSeasonInLeague(season);
        if (seasonInLeague == null)
            return;

        Policy policy = seasonInLeague.getPolicy();
        policy.setGameSettingPolicy(settingPolicy);

        boolean gameSettingPolicyField = settingPolicy.getSettingStrategy() instanceof OneMatchEachPairSettingPolicy ? true : false;
        Dao dao = DBStub.getInstance();
        dao.setGameSettingPolicy(season.getYear(), this.leagueName, gameSettingPolicyField);

        Logger logger = Logger.getInstance();
        logger.saveLog("Business.football.Game Setting policy assigned to season " + season.getYear() + " in league " + leagueName);
    }

    public void addSeasonInLeague(SeasonInLeague seasonInLeague){
        this.seasonInLeagues.add(seasonInLeague);
    }


}
