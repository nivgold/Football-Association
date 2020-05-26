package com.stubs;

import com.data.Dao;
import com.domain.logic.users.Guest;
import com.domain.logic.users.Member;
import com.domain.logic.utils.SHA1Function;
import com.logger.EventLogger;

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
        EventLogger.getInstance().saveLog("attempt to login with credentials: "+userName+" , "+password);
        Member member = dao.findMember(userName, hashPassword);
        if(member != null) {
            EventLogger.getInstance().saveLog("the guest: " + this.firstName + " login as the member: " + userName);
            // TODO upload all dependencies
            System.out.println("successful login");
        }
        else {
            EventLogger.getInstance().saveLog("the guest: " + this.firstName + " failed to login as the member: " + userName);

            throw new Exception("failed to login");
        }
        return member;
    }
}
