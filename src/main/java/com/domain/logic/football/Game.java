package com.domain.logic.football;



import com.domain.logic.enums.GameState;
import com.domain.logic.roles.Referee;
import com.domain.logic.users.IGameObserver;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * class represents a game
 */
public class Game implements IGameObservable {


    private int gameID;
    private Team host;
    private Team guest;
    private LocalDateTime date;
    private int hostTeamScore;
    private int guestTeamScore;
    private GameState gameState;
    private String report;

    private ArrayList<Event> events;
    private Field field;
    private ArrayList<IGameObserver> fanObservers;
    private ArrayList<IGameObserver> refereeObservers;
    private Referee mainReferee;
    private ArrayList<Referee> sideReferees;

    private SeasonInLeague seasonInLeague;

    static final int numOfSideReferees = 2;

    public void createReport(String report){
        this.report = report;
    }

    public Game(Team host, Team guest, SeasonInLeague seasonInLeague, LocalDateTime date, Field field){
        this.host = host;
        this.guest = guest;
        this.seasonInLeague = seasonInLeague;
        this.date = date;
        this.field = field;
        this.gameState = GameState.ToBe;
        this.report = null;

        this.events = new ArrayList<>();
        this.sideReferees = new ArrayList<>();
        this.fanObservers = new ArrayList<>();
        this.refereeObservers = new ArrayList<>();

        // TODO call dao to add a new game

        // TODO call dao to update gameID
    }

    public Game(int gameID, ArrayList<IGameObserver> gameFans){
        this.gameID = gameID;
        this.fanObservers = gameFans;
        this.events = new ArrayList<>();
        this.sideReferees = new ArrayList<>();
        this.refereeObservers = new ArrayList<>();
    }

    public void setStartGame(){
        this.gameState = GameState.OnGoing;
    }

    public void setEndGame(){
        this.gameState = GameState.Done;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void setLeague(League league) {
        this.seasonInLeague.setLeague(league);
    }

    public void setSeason(Season season){
        this.seasonInLeague.setSeason(season);
    }

    public void addSideReferee(Referee referee){
        this.sideReferees.add(referee);
    }

    @Override
    public void register(IGameObserver gameObserver) {
        if (gameObserver instanceof Referee)
            this.refereeObservers.add(gameObserver);
        else
            this.fanObservers.add(gameObserver);
    }

    @Override
    public void remove(IGameObserver gameObserver) {
        if (!this.refereeObservers.remove(gameObserver))
            this.fanObservers.remove(gameObserver);
    }


    @Override
    public void notifyGameEvent(Event event) {
        for (IGameObserver gameObserver : this.fanObservers){
            gameObserver.updateGame("New Game event in GameID:"+gameID+"!\n" +
                    "on minute: "+event.getGameMinute());
        }
    }

    @Override
    public void notifyRefereeObservers() {
        for (IGameObserver gameObserver : this.refereeObservers)
            gameObserver.updateGame("asdasd");
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Team getHost() {
        return host;
    }

    public void setHost(Team host) {
        this.host = host;
    }

    public Team getGuest() {
        return guest;
    }

    public void setGuest(Team guest) {
        this.guest = guest;
    }

    public Season getSeason() {
        return this.seasonInLeague.getSeason();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getHostTeamScore() {
        return hostTeamScore;
    }

    public void setHostTeamScore(int hostTeamScore) {
        this.hostTeamScore = hostTeamScore;
    }

    public int getGuestTeamScore() {
        return guestTeamScore;
    }

    public void setGuestTeamScore(int guestTeamScore) {
        this.guestTeamScore = guestTeamScore;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public Referee getMainReferee() {
        return mainReferee;
    }

    public void setMainReferee(Referee mainReferee) {
        this.mainReferee = mainReferee;
    }

    public ArrayList<Referee> getSideReferees() {
        return sideReferees;
    }

    public void setSideReferees(ArrayList<Referee> sideReferees) {
        this.sideReferees = sideReferees;
    }

    public League getLeague() {
        return this.seasonInLeague.getLeague();
    }

    public Field getField() {
        return field;
    }

    public ArrayList<IGameObserver> getFanObservers() {
        return fanObservers;
    }

    public void setFanObservers(ArrayList<IGameObserver> fanObservers) {
        this.fanObservers = fanObservers;
    }

    public static int getNumOfSideReferees() {
        return numOfSideReferees;
    }

    @Override
    public String toString() {
        return "Business.football.Game{" +
                "host=" + host +
                ", guest=" + guest +
                ", season=" + this.seasonInLeague.getSeason() +
                ", date=" + date +
                ", hostTeamScore=" + hostTeamScore +
                ", guestTeamScore=" + guestTeamScore +
                ", events=" + events +
                ", mainReferee=" + mainReferee +
                ", sideReferees=" + sideReferees +
                ", league=" + this.seasonInLeague.getLeague() +
                ", field=" + field +
                '}';
    }

    public ArrayList<IGameObserver> getRefereeObservers() {
        ArrayList<IGameObserver> allObservers = new ArrayList<>();
        allObservers.addAll(this.fanObservers);
        allObservers.addAll(this.refereeObservers);
        return allObservers;
    }

    public String getReport() {
        return report;
    }
}
