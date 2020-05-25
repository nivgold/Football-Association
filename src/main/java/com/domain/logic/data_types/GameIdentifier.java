package com.domain.logic.data_types;

public class GameIdentifier {
    private int gameID;
    private String hostTeamName;
    private String guestTeamName;

    public GameIdentifier(int gameID, String hostTeamName, String guestTeamName) {
        this.gameID = gameID;
        this.hostTeamName = hostTeamName;
        this.guestTeamName = guestTeamName;
    }

    public int getGameID() {
        return gameID;
    }

    public String getHostTeamName() {
        return hostTeamName;
    }

    public String getGuestTeamName() {
        return guestTeamName;
    }
}
