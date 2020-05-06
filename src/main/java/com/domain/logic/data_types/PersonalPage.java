package com.domain.logic.data_types;


import com.domain.logic.football.IPersonalPageable;
import com.domain.logic.users.IPersonalPageObserver;

import java.util.ArrayList;

public class PersonalPage implements IPersonalPageObservable {
    private static int counter = 0;

    private int id;
    private IPersonalPageable owner;
    private String description;
    private ArrayList<IPersonalPageObserver> personalPageObservers;

    public PersonalPage(IPersonalPageable owner) {
        this.owner = owner;
        this.description = owner.toString();
        this.id = counter;
        counter++;
        this.personalPageObservers = new ArrayList<>();
    }

    public void updatePage(String newData){
        this.description = newData;
    }

    public boolean removePersonalPage()
    {
        this.id=0;
        this.owner=null;
        this.description=null;
        return true;
    }


    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Business.data_types.PersonalPage{}";
    }

    @Override
    public void register(IPersonalPageObserver personalPageObserver) {
        this.personalPageObservers.add(personalPageObserver);
    }

    @Override
        public void remove(IPersonalPageObserver personalPageObserver) {
        this.personalPageObservers.remove(personalPageObserver);
    }

    @Override
    public void notifyObservers() {
        for (IPersonalPageObserver personalPageObserver : this.personalPageObservers)
            personalPageObserver.pageUpdate(this);
    }

    public IPersonalPageable getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

    public void setOwner(IPersonalPageable owner) {
        this.owner = owner;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static int getCounter() {
        return counter;
    }

    public ArrayList<IPersonalPageObserver> getPersonalPageObservers() {
        return personalPageObservers;
    }

    public static void setCounter(int counter) {
        PersonalPage.counter = counter;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPersonalPageObservers(ArrayList<IPersonalPageObserver> personalPageObservers) {
        this.personalPageObservers = personalPageObservers;
    }
}
