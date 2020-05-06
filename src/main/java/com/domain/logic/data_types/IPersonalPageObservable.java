package com.domain.logic.data_types;


import com.domain.logic.users.IPersonalPageObserver;

public interface IPersonalPageObservable {
    void register(IPersonalPageObserver personalPageObserver);
    void remove(IPersonalPageObserver personalPageObserver);
    void notifyObservers();
}
