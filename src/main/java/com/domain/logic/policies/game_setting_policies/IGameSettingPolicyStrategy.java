package com.domain.logic.policies.game_setting_policies;


import com.domain.logic.football.Game;
import com.domain.logic.football.League;
import com.domain.logic.football.Season;
import com.domain.logic.football.Team;

import java.util.ArrayList;
import java.util.HashSet;

public interface IGameSettingPolicyStrategy {

    HashSet<Game> createGames(League league, Season season, ArrayList<Team> teams);
}
