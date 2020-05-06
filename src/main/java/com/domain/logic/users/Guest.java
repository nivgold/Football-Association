package com.domain.logic.users;

import com.domain.domaincontroller.SearchSystem;
import com.domain.domaincontroller.managers.ManageMembers;
import com.domain.logic.data_types.Address;
import com.domain.logic.utils.SHA1Function;
import com.logger.Logger;

public class Guest {
    private static int countID = 0;
    private int guestID;
    private String firstName;
    private String lastName;

    public Guest(String firstName, String lastName) {
        this.guestID = countID;
        countID++;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Member login(String userName, String password){
        String hashPassword = SHA1Function.hash(password);
        ManageMembers log = ManageMembers.getInstance();
        Member member = log.findMember(userName, hashPassword);
        if(member != null)
            Logger.getInstance().saveLog("the guest: " + this.firstName + " login as the member: " + userName);
        else
            Logger.getInstance().saveLog("the guest: " + this.firstName + " failed to login as the member: " + userName);
        return member;
    }

    public Member registerAsMember(String userName, String password, String email, String country, String state, String city, String postalCode){
        ManageMembers log = ManageMembers.getInstance();
        String hashPassword = SHA1Function.hash(password);
        Member newMember = log.findMember(userName, hashPassword);
        if (newMember == null){
            Address memberAdd = new Address(country, state, city, postalCode);
            newMember = new Member(userName, hashPassword, email, memberAdd, this.firstName + " " + this.lastName);
            Logger.getInstance().saveLog("the guest: " + this.firstName + " register as the member: " + userName);
            return newMember;
        }
        Logger.getInstance().saveLog("the guest: " + this.firstName + " failed to register as the member: " + userName);
        return null;
    }

    /**
     * @param key
     * @param query
     * @return String of the wanted info
     */
    public String search(int key, String query){
        SearchSystem searcher = SearchSystem.getInstance();
        return searcher.search(key, query);
    }
}

