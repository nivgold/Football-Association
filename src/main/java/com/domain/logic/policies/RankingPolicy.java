package com.domain.logic.policies;

public class RankingPolicy {

    private Policy policy;
    private int win;
    private int goals;
    private int draw;
    private int yellowCards;
    private int redCards;


    public RankingPolicy(Policy policy){
        this.policy = policy;
    }

    public RankingPolicy(Policy policy, int win, int goals, int draw, int yellowCards, int redCards) {
        this.policy = policy;
        this.win = win;
        this.goals = goals;
        this.draw = draw;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
    }

    /**
     * updating the ranking policy parameters
     * @param win - score for wins
     * @param goals - score for goals
     * @param draw - score for draws
     * @param yellowCards - score for yellow cards
     * @param redCards - score for red cards
     */
    public void setRankingPolicy(int win, int goals, int draw, int yellowCards, int redCards){
        this.win = win;
        this.goals = goals;
        this.draw = draw;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(int yellowCards) {
        this.yellowCards = yellowCards;
    }

    public int getRedCards() {
        return redCards;
    }

    public void setRedCards(int redCards) {
        this.redCards = redCards;
    }
}
