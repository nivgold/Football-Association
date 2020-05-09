package com.domain.logic.football;

import com.domain.logic.policies.GameSettingPolicy;
import com.domain.logic.policies.Policy;
import com.domain.logic.policies.RankingPolicy;
import com.domain.logic.policies.game_setting_policies.OneMatchEachPairSettingPolicy;
import com.domain.logic.policies.game_setting_policies.TwoMatchEachPairSettingPolicy;
import com.domain.logic.roles.Referee;
import com.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class League {

    private String leagueName;
    private ArrayList<Game> games;
    private ArrayList<Season> seasons;
    private HashMap<Season, Policy> seasonLeaguePolicy;
    private HashMap<Season, ArrayList<Referee>> leagueRefereeMap; //TODO make sure to check when adding a referee to game
    private HashMap<Season, ArrayList<Team>> seasonTeamsInLeague;



    /**
     * creates a league with the given input name
     * @param leagueName
     */
    public League(String leagueName) {
        this.leagueName = leagueName;
        this.games = new ArrayList<>();
        this.seasonLeaguePolicy = new HashMap<>();
        this.seasons = new ArrayList<>();
        leagueRefereeMap = new HashMap<>();
        this.seasonTeamsInLeague = new HashMap<>();
        //TODO call DAO to add league
    }


    /**
     * registering the team to the given season in the league
     * @param season - the season we want to register the team to
     * @param team - team that we want to be registered to the season in the league
     * @return
     */
    public boolean addteamToSeasonInLeague(Season season, Team team){
        if (!seasons.contains(season)){
            return false;
        }
        if (this.seasonTeamsInLeague.get(season)== null)
            this.seasonTeamsInLeague.put(season, new ArrayList<>());
        if (this.seasonTeamsInLeague.get(season).contains(team))
            return true;

        this.seasonTeamsInLeague.get(season).add(team);
        return true;
    }


    public HashMap<Season, ArrayList<Team>> getSeasonTeamsInLeague() {
        return seasonTeamsInLeague;
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
        //checking if season exist, if not return false
        if (!seasons.contains(season)) {
            return false;
        }

        //adding referee to season in the map (creating new array list if the season is not in the list yet
        if (leagueRefereeMap.get(season)==null) { // if list doesn't exist
            leagueRefereeMap.put(season,new ArrayList<>());
        }
        if (leagueRefereeMap.get(season).contains(referee)) { //already in
            return true;
        }

        leagueRefereeMap.get(season).add(referee);
        return true;
    }

    /**
     *
     * @param game
     */
    public void addGame(Game game) {
        games.add(game);
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
            seasons.add(season);
            season.addLeague(this);

            Policy policy = new Policy(this, season);

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
        for (Season s : seasons) {
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
        if (!seasons.contains(season)) {
            throw new Exception("season not connected to league yet");
        }
        seasonLeaguePolicy.put(season, policy);
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
        Policy policy = seasonLeaguePolicy.get(season);
        policy.setRankingPolicy(win, goals, draw, yellowCards, redCards);

        //com.logger
        Logger logger = Logger.getInstance();
        logger.saveLog("Ranking Business.policies.Policy assigned to season " + season.getYear() + " in league " + leagueName);

    }

    public void setRankingPolicy(Season season, RankingPolicy rPolicy){
        Policy policy = seasonLeaguePolicy.get(season);
        policy.setRankingPolicy(rPolicy);

        //TODO call DAO to add RankingPolicy to the policy in the DB

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
        Policy policy = seasonLeaguePolicy.get(season);

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

    public void setGameSettingPolicy(Season season, GameSettingPolicy settingPolicy) {
        Policy policy = seasonLeaguePolicy.get(season);
        policy.setGameSettingPolicy(settingPolicy);

        //TODO call DAO to add GameSettingPolicy to policy in the DB

        //com.logger
        Logger logger = Logger.getInstance();
        logger.saveLog("Business.football.Game Setting policy assigned to season " + season.getYear() + " in league " + leagueName);
    }

    public String getLeagueName() {
        return leagueName;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public HashMap<Season, Policy> getSeasonLeaguePolicy() {
        return seasonLeaguePolicy;
    }

    public HashMap<Season, ArrayList<Referee>> getLeagueRefereeMap() {
        return leagueRefereeMap;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    public void setSeasons(ArrayList<Season> seasons) {
        this.seasons = seasons;
    }

    public void setSeasonLeaguePolicy(HashMap<Season, Policy> seasonLeaguePolicy) {
        this.seasonLeaguePolicy = seasonLeaguePolicy;
    }

    public void setLeagueRefereeMap(HashMap<Season, ArrayList<Referee>> leagueRefereeMap) {
        this.leagueRefereeMap = leagueRefereeMap;
    }

    public void setSeasonTeamsInLeague(HashMap<Season, ArrayList<Team>> seasonTeamsInLeague) {
        this.seasonTeamsInLeague = seasonTeamsInLeague;
    }
}
