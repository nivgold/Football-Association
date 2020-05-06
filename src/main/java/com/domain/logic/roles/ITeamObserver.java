package com.domain.logic.roles;


import com.domain.logic.enums.TeamStatus;

public interface ITeamObserver {
    void teamUpdate(TeamStatus ts);
}
