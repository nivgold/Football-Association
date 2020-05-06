package com.domain.logic.football; /**
 * class represents an event in the game- such as Goal, red card, substitution...
 */


import com.domain.logic.enums.EventType;
import com.domain.logic.roles.Player;

import java.util.Date;

public class Event {

    private Date dateTime;
    private int gameMinute;
    private String description;
    private EventType type;
    private Game game;
    private Player player;


    /**
     * constructor that gets all the parameters
     */
    public Event(int gameMinute, String description, EventType type, Date dateTime, Game game, Player player) {
        this.gameMinute = gameMinute;
        this.description = description;
        this.type = type;
        this.dateTime = dateTime;
        this.game = game;
        this.player = player;
    }

    /**
     * counstructor that makes the date stamp automatically
     */
    public Event(int gameMinute, String description, EventType type, Game game, Player player) {
        this.gameMinute = gameMinute;
        this.description = description;
        this.type = type;
        this.game = game;
        this.player = player;
        dateTime = new Date();
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int getGameMinute() {
        return gameMinute;
    }

    public void setGameMinute(int gameMinute) {
        this.gameMinute = gameMinute;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
