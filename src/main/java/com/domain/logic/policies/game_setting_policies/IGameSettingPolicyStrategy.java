package com.domain.logic.policies.game_setting_policies;


import com.domain.logic.football.*;

import java.util.ArrayList;
import java.util.HashSet;

public interface IGameSettingPolicyStrategy {

    HashSet<Game> createGames(SeasonInLeague seasonInLeague, ArrayList<Team> teams);
}
