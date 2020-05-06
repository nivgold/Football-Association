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

    private Team host;
    private Team guest;
    private Season season;
    private LocalDateTime date;
    private int hostTeamScore;
    private int guestTeamScore;
    private ArrayList<Event> events;
    private Referee mainReferee;
    private ArrayList<Referee> sideReferees;
    private League league;
    private Field field;
    private ArrayList<IGameObserver> fanObservers;
    private ArrayList<IGameObserver> refereeObservers;
    private GameState gameState;


    static final int numOfSideReferees = 2;




    public Game(Team host, Team guest, Season season, League league, LocalDateTime date, Field field){
        this.host = host;
        this.guest = guest;
        this.season = season;
        this.league = league;
        this.date = date;
        this.field = field;
        this.gameState = GameState.ToBe;

        this.events = new ArrayList<>();
        this.sideReferees = new ArrayList<>();
        this.fanObservers = new ArrayList<>();
        this.refereeObservers = new ArrayList<>();
    }

    /**
     * constructor receiving all params as input
     * @param host
     * @param guest
     * @param date
     * @param hostTeamScore
     * @param guestTeamScore
     * @param events
     * @param mainReferee
     * @param sideReferees
     * @param league
     */
    public Game(Team host, Team guest, LocalDateTime date, int hostTeamScore, int guestTeamScore,
                ArrayList<Event> events, Referee mainReferee, ArrayList<Referee> sideReferees,
                League league, Field field) {
        this.host = host;
        this.guest = guest;
        this.date = date;
        this.hostTeamScore = hostTeamScore;
        this.guestTeamScore = guestTeamScore;
        this.events = events;
        this.mainReferee = mainReferee;
        this.sideReferees = sideReferees;
        this.league = league;
        this.field=field;
        this.gameState = GameState.ToBe;

        this.events = new ArrayList<>();
        this.sideReferees = new ArrayList<>();
        this.fanObservers = new ArrayList<>();
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
        this.league = league;
    }

    public void setSeason(Season season){
        this.season = season;
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
    public void notifyFanObservers() {
        for (IGameObserver gameObserver : this.fanObservers)
            gameObserver.updateGame(this);
    }

    @Override
    public void notifyRefereeObservers() {
        for (IGameObserver gameObserver : this.refereeObservers)
            gameObserver.updateGame(this);
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
        return season;
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
        return league;
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
                ", season=" + season +
                ", date=" + date +
                ", hostTeamScore=" + hostTeamScore +
                ", guestTeamScore=" + guestTeamScore +
                ", events=" + events +
                ", mainReferee=" + mainReferee +
                ", sideReferees=" + sideReferees +
                ", league=" + league +
                ", field=" + field +
                '}';
    }

    public ArrayList<IGameObserver> getRefereeObservers() {
        ArrayList<IGameObserver> allObservers = new ArrayList<>();
        allObservers.addAll(this.fanObservers);
        allObservers.addAll(this.refereeObservers);
        return allObservers;
    }
}
