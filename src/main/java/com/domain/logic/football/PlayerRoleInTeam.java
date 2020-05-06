package com.domain.logic.football;


import com.domain.logic.enums.PlayerRole;
import com.domain.logic.roles.Player;

public class PlayerRoleInTeam {

    Player player;
    Team team;
    PlayerRole playerRole;

    public PlayerRoleInTeam(Player player, Team team, PlayerRole playerRole) {
        this.player = player;
        this.team = team;
        this.playerRole = playerRole;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public PlayerRole getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(PlayerRole playerRole) {
        this.playerRole = playerRole;
    }
}
