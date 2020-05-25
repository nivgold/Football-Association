package com.domain.logic.policies;


import com.domain.logic.football.League;
import com.domain.logic.football.Season;
import com.domain.logic.football.SeasonInLeague;
import com.domain.logic.policies.game_setting_policies.IGameSettingPolicyStrategy;

public class Policy {

    private GameSettingPolicy gameSettingPolicy;
    private RankingPolicy rankingPolicy;

    private SeasonInLeague seasonInLeague;

    public Policy(SeasonInLeague seasonInLeague){
        this.seasonInLeague = seasonInLeague;
        this.gameSettingPolicy = null;
        this.rankingPolicy = null;
    }

    public Policy(SeasonInLeague seasonInLeague, GameSettingPolicy gameSettingPolicy, RankingPolicy rankingPolicy){
        this.seasonInLeague = seasonInLeague;
        this.gameSettingPolicy = gameSettingPolicy;
        this.rankingPolicy = rankingPolicy;
    }

    /**
     * setting the ranking policy
     * @param rankingPolicy - the ranking policy of the league-season policy
     */
    public void setRankingPolicy(RankingPolicy rankingPolicy){
        this.rankingPolicy = rankingPolicy;
    }

    /**
     * setting the ranking policy by setting all the parameters for ranking policy
     * @param win - score for wins
     * @param goals - score for goals
     * @param draw - score for draws
     * @param yellowCards - score for yellow cards
     * @param redCards - score for red cards
     */
    public void setRankingPolicy(int win, int goals, int draw, int yellowCards, int redCards){
        this.rankingPolicy = new RankingPolicy(this, win, goals, draw, yellowCards, redCards);
    }



    /**
     * setting the game setting policy
     * @param settingStrategy - the game setting policy of the league-season policy
     */
    public void setGameSettingPolicy(IGameSettingPolicyStrategy settingStrategy){
        this.gameSettingPolicy = new GameSettingPolicy(this, settingStrategy);
    }

    /**
     * Business.football.Game Setting Business.policies.Policy Getter
     * @return
     */
    public GameSettingPolicy getGameSettingPolicy() {
        return this.gameSettingPolicy;
    }

    /**
     * Ranking Business.policies.Policy Getter
     * @return
     */
    public RankingPolicy getRankingPolicy(){
        return this.rankingPolicy;
    }

    /**
     * Business.football.League Getter
     * @return
     */
    public League getLeague() {
        return this.seasonInLeague.getLeague();
    }

    /**
     * Business.football.Season Getter
     * @return
     */
    public Season getSeason() {
        return this.seasonInLeague.getSeason();
    }

    public void setLeague(League league) {
        this.seasonInLeague.setLeague(league);
    }

    public void setSeason(Season season) {
        this.seasonInLeague.setSeason(season);
    }

    public void setGameSettingPolicy(GameSettingPolicy gameSettingPolicy) {
        this.gameSettingPolicy = gameSettingPolicy;
    }

    public SeasonInLeague getSeasonInLeague() {
        return seasonInLeague;
    }
}
