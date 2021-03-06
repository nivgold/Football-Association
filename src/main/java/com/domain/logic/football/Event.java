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

    private String playerUsername;
    private int gameID;
    private String hostTeamName;
    private String guestTeamName;


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

        this.player.getMember().getUserName();
        this.gameID = game.getGameID();
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

        this.player.getMember().getUserName();
        this.gameID = game.getGameID();
    }

    public Event(int gameMinute, String description, EventType type, int gameID, String hostTeamName, String guestTeamName, String playerUsername){
        this.gameMinute = gameMinute;
        this.description = description;
        this.type = type;
        this.gameID = gameID;
        this.playerUsername = playerUsername;
        this.hostTeamName = hostTeamName;
        this.guestTeamName = guestTeamName;
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

    public String getPlayerUsername() {
        return playerUsername;
    }

    public int getGameID() {
        return gameID;
    }

    @Override
    public String toString() {
        return "New game event happened in the game between :"+hostTeamName+" VS. "+guestTeamName+"\n" +
                "Event Type: "+type.name()+"\n" +
                "Description: "+description+"\n" +
                "Player Involved: "+playerUsername;
    }
}
