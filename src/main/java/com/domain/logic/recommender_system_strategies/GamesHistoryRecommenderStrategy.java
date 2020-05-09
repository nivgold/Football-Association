package com.domain.logic.recommender_system_strategies;

import com.domain.logic.football.Game;
import com.domain.logic.football.Season;
import com.domain.logic.football.Team;

@SuppressWarnings("Duplicates")
public class GamesHistoryRecommenderStrategy implements IRecommenderSystemStrategy {

    public GamesHistoryRecommenderStrategy() {
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public double getChances(Game game) {
        Team hostTeam = game.getHost();
        Team guestTeam = game.getGuest();
        // computing the win rate of the host team
        Season currentSeason = game.getSeason();
        int hostTeamTotalGames=0, guestTeamTotalGames=0, hostTeamWins=0, guestTeamWins=0;
        if (currentSeason.getGames()==null || currentSeason.getGames().isEmpty())
            return 0.5;
        for (Game previousGame: currentSeason.getGames()){
            if (previousGame.getHost().equals(hostTeam) || previousGame.getGuest().equals(hostTeam)){
                hostTeamTotalGames++;
                if (previousGame.getHost().equals(hostTeam)){
                    if (previousGame.getHostTeamScore()>previousGame.getGuestTeamScore())
                        // host won on previous game
                        hostTeamWins++;
                }
                else{
                    if (previousGame.getGuestTeamScore()>previousGame.getHostTeamScore())
                        // guest won on previous game
                        hostTeamWins++;
                }
            }
            if (previousGame.getHost().equals(guestTeam) || previousGame.getGuest().equals(guestTeam)){
                guestTeamTotalGames++;
                if (previousGame.getHost().equals(guestTeam)){
                    if (previousGame.getHostTeamScore()>previousGame.getGuestTeamScore())
                        // host won on previous game
                        guestTeamWins++;
                }
                else{
                    if (previousGame.getGuestTeamScore()>previousGame.getHostTeamScore())
                        // guest won on previous game
                        guestTeamWins++;
                }
            }
        }

        double hostWinRate = hostTeamWins / hostTeamTotalGames;
        double guestWinRate = guestTeamWins / guestTeamTotalGames;

        return hostWinRate / (hostWinRate+guestWinRate);
    }

}
