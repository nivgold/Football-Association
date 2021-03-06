package com.domain.logic.users;

import com.data.DBCommunicator;
import com.data.Dao;
import com.domain.logic.AssociationSystem;
import com.domain.logic.SearchSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.utils.SHA1Function;
import com.logger.EventLogger;

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

    public Member login(String userName, String password) throws Exception {
        String hashPassword = SHA1Function.hash(password);
        Dao dao = DBCommunicator.getInstance();
        Member member = dao.findMember(userName, hashPassword);
        if(member != null) {
            AssociationSystem.getInstance().connectUser(member);
        }
        else {
            throw new Exception("guest failed to login with the credentials: \""+userName+"\" , \""+password+"\"");
        }
        return member;
    }

    public Member registerAsMember(String userName, String password, String email, String country, String state, String city, String postalCode){
        Dao dao = DBCommunicator.getInstance();
        String hashPassword = SHA1Function.hash(password);
        Member newMember = null;
        try {
            newMember = dao.findMember(userName, hashPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (newMember == null){
            Address memberAdd = new Address(country, state, city, postalCode);
            //TODO add new member to dao
            newMember = new Member(userName, hashPassword, email, memberAdd, this.firstName + " " + this.lastName);
            EventLogger.getInstance().saveLog("the guest: " + this.firstName + " register as the member: " + userName);
            return newMember;
        }
        EventLogger.getInstance().saveLog("the guest: " + this.firstName + " failed to register as the member: " + userName);
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

