package com.stubs;


import com.domain.logic.enums.PlayerRole;
import com.domain.logic.football.PlayerRoleInTeam;
import com.domain.logic.football.Team;
import com.domain.logic.roles.Player;

public class PlayerRoleInTeamStub extends PlayerRoleInTeam {

    Player player;
    Team team;
    PlayerRole playerRole;

    public PlayerRoleInTeamStub(Player p, Team t, PlayerRole pr) {
        super(p, t, pr);
        this.player = p;
        this.team = t;
        this.playerRole = pr;
    }

    public Player getPlayer() {
        return player;
    }

    public Team getTeam() {
        return team;
    }

    public PlayerRole getPlayerRole() {
        return playerRole;
    }
}
