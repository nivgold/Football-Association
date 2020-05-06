package com.domain.logic.policies.game_setting_policies;


import com.domain.logic.football.Game;
import com.domain.logic.football.League;
import com.domain.logic.football.Season;
import com.domain.logic.football.Team;
import com.domain.logic.roles.Referee;

import java.util.ArrayList;
import java.util.HashSet;

@SuppressWarnings("Duplicates")
public class OneMatchEachPairSettingPolicy implements IGameSettingPolicyStrategy {


    public OneMatchEachPairSettingPolicy() {
    }

    @Override
    public HashSet<Game> createGames(League league, Season season, ArrayList<Team> teams) {
        HashSet<Game> games = new HashSet<>();
        ArrayList<Referee> leagueReferees = league.getLeagueRefereeMap().get(season);
        int refereeIndex = 0;
        int coin = 0;
        for (int i=0; i<teams.size(); i++){
            for (int j=i+1; j<teams.size(); j++){
                Game game;
                if (coin%2 == 0){
                    game = new Game(teams.get(i), teams.get(j), season, league, null, teams.get(i).getField());
                }
                else{
                    game = new Game(teams.get(j), teams.get(i), season, league, null, teams.get(j).getField());
                }
                coin++;
                if (leagueReferees!=null && leagueReferees.size()>0) { //adding referries only if exist
                    Referee main = leagueReferees.get(refereeIndex);
                    refereeIndex = (refereeIndex + 1) % leagueReferees.size();
                    game.setMainReferee(main);
                    if (leagueReferees.size()>=Game.getNumOfSideReferees()) {
                        for (int numOfSideReferees = 0; numOfSideReferees < Game.getNumOfSideReferees(); numOfSideReferees++) {
                            Referee side = leagueReferees.get(refereeIndex);
                            refereeIndex = (refereeIndex + 1) % leagueReferees.size();
                            game.addSideReferee(side);
                        }
                    }
                }
                games.add(game);
            }
        }

        return games;
    }
}
