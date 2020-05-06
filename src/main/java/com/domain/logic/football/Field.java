package com.domain.logic.football;


import com.domain.logic.data_types.Address;

import java.util.ArrayList;

public class Field {

    private Team ownerTeam;
    private Address location;
    private ArrayList<Game> games;

    public Field(String country, String state, String city, String postalCode) {
        this.location = new Address(country, state, city, postalCode);
        games = new ArrayList<>();
    }

    public void addGame(Game g){
        games.add(g);
    }

    /**
     * sets owner team
     * @param ownerTeam
     */
    public void setOwnerTeam(Team ownerTeam){
        this.ownerTeam=ownerTeam;
    }

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    public Team getOwnerTeam() {
        return ownerTeam;
    }

}
