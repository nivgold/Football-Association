package com.domain.logic.policies.game_setting_policies;


import com.domain.logic.football.*;
import com.domain.logic.roles.Referee;

import java.util.ArrayList;
import java.util.HashSet;

public class TwoMatchEachPairSettingPolicy implements IGameSettingPolicyStrategy {

    public TwoMatchEachPairSettingPolicy() {
    }

    @Override
    public HashSet<Game> createGames(SeasonInLeague seasonInLeague, ArrayList<Team> teams) {
        League league = seasonInLeague.getLeague();
        Season season = seasonInLeague.getSeason();
        HashSet<Game> games = new HashSet<>();
        ArrayList<Referee> leagueReferees = league.getLeagueRefereeMap().get(season);
        int refereeIndex = 0;

        for (int i=0; i<teams.size(); i++){
            for (int j=i+1; j<teams.size(); j++){
                Game game1 = new Game(teams.get(i), teams.get(j), seasonInLeague, null, teams.get(i).getField());
                appointReferees(game1, leagueReferees, refereeIndex);
                Game game2 = new Game(teams.get(j), teams.get(i), seasonInLeague, null, teams.get(j).getField());
                appointReferees(game2, leagueReferees, refereeIndex);
                games.add(game1);
                games.add(game2);
            }
        }

        return games;
    }
    private void appointReferees(Game game, ArrayList<Referee> leagueReferees, int refereeIndex){
        if (leagueReferees==null || leagueReferees.size()==0)
            return;
        Referee mainReferee = leagueReferees.get(refereeIndex);
        refereeIndex = (refereeIndex + 1) % leagueReferees.size();
        game.setMainReferee(mainReferee);

        if (leagueReferees.size()<Game.getNumOfSideReferees())
            return;
        for (int numOfSideReferees=0; numOfSideReferees<Game.getNumOfSideReferees(); numOfSideReferees++){
            Referee side = leagueReferees.get(refereeIndex);
            refereeIndex = (refereeIndex+1) % leagueReferees.size();
            game.addSideReferee(side);
        }
    }
}
