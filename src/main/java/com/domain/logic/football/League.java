package com.domain.logic.football;

import com.data.DBCommunicator;
import com.data.Dao;
import com.domain.logic.policies.GameSettingPolicy;
import com.domain.logic.policies.Policy;
import com.domain.logic.policies.RankingPolicy;
import com.domain.logic.policies.game_setting_policies.OneMatchEachPairSettingPolicy;
import com.domain.logic.policies.game_setting_policies.TwoMatchEachPairSettingPolicy;
import com.domain.logic.roles.Referee;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class League {

    private String leagueName;
    private ArrayList<SeasonInLeague> seasonInLeagues;



    /**
     * creates a league with the given input name
     * @param leagueName
     */
    public League(String leagueName) {
        this.leagueName = leagueName;
        seasonInLeagues = new ArrayList<>();
        //TODO call DAO to add league
    }


    /**
     * registering the team to the given season in the league
     * @param season - the season we want to register the team to
     * @param team - team that we want to be registered to the season in the league
     * @return
     */
    public boolean addTeamToSeasonInLeague(Season season, Team team){
        SeasonInLeague seasonInLeague = findSeasonInLeague(season);
        if (seasonInLeague == null)
            return false;
        seasonInLeague.addTeam(team);
        return true;
    }

    public SeasonInLeague findSeasonInLeague(Season season){
        for (SeasonInLeague seasonInLeague : this.seasonInLeagues){
            if (seasonInLeague.getSeason().getYear()==season.getYear())
                return seasonInLeague;
        }
        return null;
    }

    /**
     * Adding the input referee to the input season in the season-referee map of the league.
     * If the season is not in the {@code seasons} list, returns false. else, returns true (even if referee already in).
     * If the season is not yet exists as a key in the map, it will be added.
     * @param season
     * @param referee
     * @return
     */
    public boolean addRefereeToSeasonInLeague(Season season, Referee referee) {
        SeasonInLeague seasonInLeague = findSeasonInLeague(season);
        if (seasonInLeague == null)
            return false;
        seasonInLeague.addReferee(referee);
        return true;
    }

    /**
     *
     * @param game
     */
    public void addGame(Game game) {
        SeasonInLeague seasonInLeague = findSeasonInLeague(game.getSeason());
        if (seasonInLeague == null)
            return;
        seasonInLeague.addGame(game);
        game.setLeague(this);

        //write log
        Logger logger = Logger.getInstance();
        logger.saveLog("Business.football.Game between host " + game.getHost().getTeamName() + " and " + game.getGuest().getTeamName() +
                                                        " in " + game.getDate().toString() + " added to " + this.getLeagueName());

    }

    /**
     * connecting a season to a league. updates the season's list also
     * @param season
     */
    public void addSeason(Season season) {
        if (!containingSeasonWithYear(season.getYear())) {
            // added
            SeasonInLeague seasonInLeague = new SeasonInLeague(this, season);
            // added

            Policy policy = new Policy(seasonInLeague);

            try {
                setPolicyToSeason(season, policy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //com.logger
        Logger logger = Logger.getInstance();
        logger.saveLog("Business.football.Season " + season.getYear() + "added to: " + getLeagueName());
    }

    private boolean containingSeasonWithYear(int year) {
        for (SeasonInLeague seasonInLeague : this.seasonInLeagues) {
            Season s = seasonInLeague.getSeason();
            if (s.getYear()==year)
                return true;
        }
        return false;
    }

    /**
     * connecting the policy to the season in this league. Note that the method responsible for
     * updating the maps in the season also.
     * @param season
     * @param policy Containing the Business.policies.GameSettingPolicy and Business.policies.RankingPolicy
     * @throws Exception if the input season is not connected to the league
     */
    public void setPolicyToSeason(Season season, Policy policy) throws Exception {
        SeasonInLeague seasonInLeague = findSeasonInLeague(season);
        if (seasonInLeague == null) {
            throw new Exception("season not connected to league yet");
        }
        seasonInLeague.setPolicy(policy);
        season.setPolicyToLeague(this, policy);

        //TODO call DAO to add new policy of league-season to DB

        //com.logger
        Logger logger = Logger.getInstance();
        logger.saveLog("Policy assigned to season " + season.getYear() + " in league " + leagueName);
    }

    /**
     * setting a ranking policy for a season in the league
     * @param season - the season we want to update its policy
     * @param win - score for wins
     * @param goals - score for goals
     * @param draw - score for draws
     * @param yellowCards - score for yellow cards
     * @param redCards - score for red cards
     */
    public void setRankingPolicy(Season season, int win, int goals, int draw, int yellowCards, int redCards){
        SeasonInLeague seasonInLeague = findSeasonInLeague(season);
        if (seasonInLeague == null)
            return;
        Policy policy = seasonInLeague.getPolicy();
        policy.setRankingPolicy(win, goals, draw, yellowCards, redCards);

        //com.logger
        Logger logger = Logger.getInstance();
        logger.saveLog("Ranking Business.policies.Policy assigned to season " + season.getYear() + " in league " + leagueName);

    }

    public void setRankingPolicy(Season season, RankingPolicy rPolicy) throws Exception {
        SeasonInLeague seasonInLeague = findSeasonInLeague(season);
        if (seasonInLeague == null)
            return;

        Policy policy = seasonInLeague.getPolicy();
        policy.setRankingPolicy(rPolicy);

        //TODO call DAO to add RankingPolicy to the policy in the DB
        Dao dao = DBCommunicator.getInstance();
        dao.setGameRankingPolicy(season.getYear(), this.leagueName, rPolicy.getWin(), rPolicy.getGoals(), rPolicy.getDraw(), rPolicy.getYellowCards(), rPolicy.getRedCards());
        //com.logger
        Logger logger = Logger.getInstance();
        logger.saveLog("Ranking Business.policies.Policy assigned to season " + season.getYear() + " in league " + leagueName);
    }




    /**
     * setting a game setting policy for a season on the league
     * @param season - the season we want to update its policy
     * @param settingPolicy - the wanted game setting policy
     */
    public void setGameSettingPolicy(Season season, String settingPolicy){
        SeasonInLeague seasonInLeague = findSeasonInLeague(season);
        if (seasonInLeague == null)
            return;

        Policy policy = seasonInLeague.getPolicy();

        switch (settingPolicy){
            case "onematch":
                policy.setGameSettingPolicy(new OneMatchEachPairSettingPolicy());
                break;
            case "twomatch":
                policy.setGameSettingPolicy(new TwoMatchEachPairSettingPolicy());
                break;
            default:
                System.out.println("No Matching Business.football.Game Setting Business.policies.Policy");
                break;
        }

        //com.logger
        Logger logger = Logger.getInstance();
        logger.saveLog("Business.football.Game Setting policy assigned to season " + season.getYear() + " in league " + leagueName);
    }

    public void setGameSettingPolicy(Season season, GameSettingPolicy settingPolicy) throws Exception {
        SeasonInLeague seasonInLeague = findSeasonInLeague(season);
        if (seasonInLeague == null)
            return;

        Policy policy = seasonInLeague.getPolicy();
        policy.setGameSettingPolicy(settingPolicy);

        //TODO call DAO to add GameSettingPolicy to policy in the DB
        boolean gameSettingPolicyField;
        if(settingPolicy.getSettingStrategy() instanceof OneMatchEachPairSettingPolicy)
            gameSettingPolicyField = false;
        else
            gameSettingPolicyField = true;
        Dao dao = DBCommunicator.getInstance();
        dao.setGameSettingPolicy(season.getYear(), this.leagueName, gameSettingPolicyField);

        Logger logger = Logger.getInstance();
        logger.saveLog("Business.football.Game Setting policy assigned to season " + season.getYear() + " in league " + leagueName);
    }

    public void addSeasonInLeague(SeasonInLeague seasonInLeague){
        this.seasonInLeagues.add(seasonInLeague);
    }

    public String getLeagueName() {
        return leagueName;
    }

    public ArrayList<Game> getGames() {
        ArrayList<Game> games = new ArrayList<>();
        for (SeasonInLeague seasonInLeague : this.seasonInLeagues)
            games.addAll(seasonInLeague.getGames());
        return games;
    }

    public ArrayList<Season> getSeasons() {
        ArrayList<Season> seasons = new ArrayList<>();
        for (SeasonInLeague seasonInLeague : this.seasonInLeagues)
            seasons.add(seasonInLeague.getSeason());
        return seasons;
    }

    public HashMap<Season, Policy> getSeasonLeaguePolicy() {
        HashMap<Season, Policy> hashMap = new HashMap<>();
        for (SeasonInLeague seasonInLeague : this.seasonInLeagues){
            hashMap.put(seasonInLeague.getSeason(), seasonInLeague.getPolicy());
        }
        return hashMap;
    }

    public HashMap<Season, ArrayList<Referee>> getLeagueRefereeMap() {
        HashMap<Season, ArrayList<Referee>> hashMap = new HashMap<>();
        for (SeasonInLeague seasonInLeague : this.seasonInLeagues){
            hashMap.put(seasonInLeague.getSeason(), seasonInLeague.getReferees());
        }
        return hashMap;
    }
}
