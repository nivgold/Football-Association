package com.domain.domaincontroller;

import com.domain.domaincontroller.recommender_system_strategies.IRecommenderSystemStrategy;
import com.domain.logic.football.Game;

public class RecommenderSystem {
    private static RecommenderSystem recommenderSystem;

    private IRecommenderSystemStrategy recommenderSystemStrategy;

    private RecommenderSystem() {
    }

    public static RecommenderSystem getInstance()
    {
        if (recommenderSystem == null)
            recommenderSystem = new RecommenderSystem();
        return recommenderSystem;
    }

    /**
     * letting the a Business.users.SystemManagerMember to build a model for the recommender system
     */
    public void BuildRecommenderSystem(IRecommenderSystemStrategy recommenderSystemStrategy){
        this.recommenderSystemStrategy = recommenderSystemStrategy;
    }

    /**
     *
     * @param game - represent the wanted game
     * @return the chances of the host team to win the game
     */
    public double getChances(Game game){
        return this.recommenderSystemStrategy.getChances(game);
    }
}
