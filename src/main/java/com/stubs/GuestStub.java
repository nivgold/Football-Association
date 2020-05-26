package com.stubs;

import com.data.DBCommunicator;
import com.data.Dao;
import com.domain.logic.AssociationSystem;
import com.domain.logic.users.Guest;
import com.domain.logic.users.Member;
import com.domain.logic.utils.SHA1Function;
import com.logger.Logger;

public class GuestStub extends Guest {

    private String firstName;
    private String lastName;

    public GuestStub(String firstName, String lastName) {
        super(firstName, lastName);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Member login(String userName, String password) throws Exception {
        String hashPassword = SHA1Function.hash(password);
        Dao dao = DBStub.getInstance();
        Logger.getInstance().saveLog("attempt to login with credentials: "+userName+" , "+password);
        Member member = dao.findMember(userName, hashPassword);
        if(member != null) {
            Logger.getInstance().saveLog("the guest: " + this.firstName + " login as the member: " + userName);
            // TODO upload all dependencies
            System.out.println("successful login");
        }
        else {
            Logger.getInstance().saveLog("the guest: " + this.firstName + " failed to login as the member: " + userName);

            throw new Exception("failed to login");
        }
        return member;
    }
}
