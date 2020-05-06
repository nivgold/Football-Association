package com.domain.logic.policies;


import com.domain.logic.football.Game;
import com.domain.logic.football.League;
import com.domain.logic.football.Season;
import com.domain.logic.football.Team;
import com.domain.logic.policies.game_setting_policies.IGameSettingPolicyStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class GameSettingPolicy {

    private IGameSettingPolicyStrategy settingStrategy;
    private Policy policy;

    public GameSettingPolicy(Policy policy, IGameSettingPolicyStrategy strategy){
        this.policy = policy;
        this.settingStrategy = strategy;
    }

    /**
     * creating the games for the Business.policies.Policy of the season and the league
     * @return returns the games of the season in the league
     */
    public ArrayList<Game> createGames(){
        League league = policy.getLeague();
        Season season = policy.getSeason();
        ArrayList<Team> teams = league.getSeasonTeamsInLeague().get(season);
        HashSet<Game> games = settingStrategy.createGames(league, season, teams);
        ArrayList<Game> result = new ArrayList<>();
        LocalDateTime baseDate = LocalDateTime.now();
        baseDate = baseDate.plusDays(30);
        Iterator<Game> iterator = games.iterator();
        int i=0;
        while (iterator.hasNext()){
            Game game = iterator.next();
            game.setDate(baseDate.plusDays(i));
            i++;
            result.add(game);
        }
        return result;
    }

    public IGameSettingPolicyStrategy getSettingStrategy() {
        return settingStrategy;
    }

    public void setSettingStrategy(IGameSettingPolicyStrategy settingStrategy) {
        this.settingStrategy = settingStrategy;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }
}
