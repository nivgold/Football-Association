package com.domain.logic.football;


import com.domain.logic.users.IGameObserver;

public interface IGameObservable {
    void register(IGameObserver gameObserver);
    void remove(IGameObserver gameObserver);
    void notifyFanObservers();
    void notifyRefereeObservers();
}
