package com.domain.logic.recommender_system_strategies;


import com.domain.logic.football.Game;

public interface IRecommenderSystemStrategy {
    double getChances(Game game);
}
