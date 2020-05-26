package com.domain.logic.roles;

import com.data.DBCommunicator;
import com.data.Dao;
import com.domain.logic.enums.EventType;
import com.domain.logic.football.Event;
import com.domain.logic.football.Game;
import com.domain.logic.football.League;
import com.domain.logic.users.IGameObserver;
import com.domain.logic.users.Member;
import com.logger.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Referee implements IRole, IGameObserver {

    private String qualification;
    private Member member;
    private ArrayList<League> leagues;
    private ArrayList<Game> main;
    private ArrayList<Game> side;

    public Referee(Member member) throws Exception {
        this.qualification = "New";
        this.member = member;
        this.leagues = new ArrayList<>();
        this.main = new ArrayList<>();
        this.side = new ArrayList<>();
        member.addReferee(this);
        //TODO call the DAO to add new referee to the DB
    }

    /**
     * add new event to ongoing game.
     *
     * @param gameMinute
     * @param description
     * @param type
     * @param dateTime
     * @param game
     * @param player
     */
    public boolean createGameEvent(int gameMinute, String description, EventType type, Date dateTime, Game game, Player player) {
        //check if the referee is authorized to judge this game
        boolean isAuthorized = main.contains(game) || side.contains(game);
        if (isAuthorized) {
            Event e = new Event(gameMinute, description, type, dateTime, game, player);
            if (type==EventType.GuestGoal){
                game.setGuestTeamScore(game.getGuestTeamScore()+1);
            }
            else if (type==EventType.HostGoal){
                game.setHostTeamScore(game.getHostTeamScore()+1);
            }
            game.getEvents().add(e);
            //TODO call DAO to add new event to game
            return true;
        } else {
            Logger.getInstance().saveLog(member.getUserName() + " referee is not authorized to add events to the game");
            return false;
        }
    }

    public void createGameEvent(int gameMinute, String description, EventType type, int gameID, String playerUsername) throws Exception {
        if (isAuthorized(gameID)){
            // 0 - no score change
            // 1 - add goal to host team
            // -1 - add goal to guest team
            int changeScore = 0;
            if (type == EventType.HostGoal){
                changeScore = 1;
            }
            else if (type == EventType.GuestGoal){
                changeScore = -1;
            }
            Dao dao = DBCommunicator.getInstance();
            dao.addGameEvent(gameID, gameMinute, description, type, playerUsername, changeScore);
        }
    }

    private boolean isAuthorized(int gameID) throws Exception {
        return DBCommunicator.getInstance().isRefereeAuthorized(this.member.getUserName(), gameID);
    }

    private boolean isReportAuthorized(int gameID) throws Exception {
        return DBCommunicator.getInstance().isReportAuthorized(this.member.getUserName(), gameID);
    }

    public void createReport(int gameID, String report) throws Exception {
        if (isReportAuthorized(gameID)){
            Dao dao = DBCommunicator.getInstance();
            dao.setGameReport(gameID, report);
        }
    }

    /**
     * edit an event of finished game.
     *
     * @param g
     * @param oldEvent
     * @param newEvent
     */
    public void editGameEvent(Game g, Event oldEvent, Event newEvent) {
        //check if the edit time is valid
        boolean validTime = (LocalDateTime.now().getHour() - g.getDate().getHour()) <= 5;
        if (validTime) {
            //check if the referee is authorized to judge this game
            boolean isAuthorized = main.contains(g);
            if (isAuthorized) {
                //checks if the event we want to edit is exists
                boolean isExists = g.getEvents().contains(oldEvent);
                if (isExists) {
                    g.getEvents().remove(oldEvent);
                }
                g.getEvents().add(newEvent);
            } else {
                Logger.getInstance().saveLog("This referee is not authorized to change events of this game");
            }
        } else {
            Logger.getInstance().saveLog("The valid time to make changes to this game is over");
        }

    }

    /**
     * show the referee the game details.
     * only for game he was assigned to.
     *
     * @param g
     * @return
     */
    public String watchGameDetails(Game g) {
        String ans = "";
        if (main.contains(g) || side.contains(g)) {
            ans = g.toString();
        } else {
            System.out.println("You are not assigned to this game");
            ;
        }
        return ans;
    }

    /**
     * prints all the upcoming game scheduling
     */
    public String getSchedulingDetails() {
        String ans = "";
        //System.out.println("Your scheduling games details");
        //System.out.println("Main Business.roles.Referee: ");
        for (Game g : main) {
            if (g.getDate().isAfter(LocalDateTime.now())) {
                ans += g.toString();
            }
        }
        //System.out.println("Side Business.roles.Referee: ");
        for (Game g : side) {
            if (g.getDate().isAfter(LocalDateTime.now())) {
                ans += g.toString();
            }
        }
        return ans;
    }

    /**
     * remove the object from all occurrences
     *
     * @return
     */
    @Override
    public boolean removeYourself() throws Exception {

        for (League l : leagues) {
            for (ArrayList<Referee> rl : l.getLeagueRefereeMap().values()) {
                if (rl.contains(this)) {
                    rl.remove(this);
                }
            }
        }
        member.removeReferee(this);
        this.removeRefereeFromGames();
        main.clear();
        side.clear();
        Logger.getInstance().saveLog("The referee has been removed successfully");
        return true;

    }

    private void removeRefereeFromGames() {
        for (Game g : getSide()) {
            g.getSideReferees().remove(this);
        }
        main.clear();
        side.clear();
    }

    public ArrayList<League> getLeagues() {
        return leagues;
    }

    public Member getMember() {
        return member;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setLeagues(ArrayList<League> leagues) {
        this.leagues = leagues;
    }

    public ArrayList<Game> getMain() {
        return main;
    }

    public void setMain(ArrayList<Game> main) {
        this.main = main;
    }

    public ArrayList<Game> getSide() {
        return side;
    }

    public void setSide(ArrayList<Game> side) {
        this.side = side;
    }

    @Override
    public void updateGame(Game game) {
        System.out.println("The game date is updated to " + game.getDate());
    }

    public void registerToGame(Game g) {
        g.register(this);
    }

    public void unregisterFromGame(Game g) {
        g.remove(this);
    }

    public void addMainGame(Game g) {
        this.getMain().add(g);
        g.setMainReferee(this);
    }

    public void addSideGame(Game g) {
        this.getSide().add(g);
        g.getSideReferees().add(this);
    }
}
