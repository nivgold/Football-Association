package com.domain.logic.football;


import com.domain.logic.roles.ITeamObserver;

public interface ITeamObservable {
    void register(ITeamObserver teamObserver);
    void remove(ITeamObserver teamObserver);
    void notifyObservers();
}
