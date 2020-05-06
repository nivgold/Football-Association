package com.stubs;


import com.domain.logic.data_types.Complaint;
import com.domain.logic.users.Member;
import com.domain.logic.users.SystemManagerMember;

public class ComplaintStub extends Complaint {

    public ComplaintStub(Member complainer, String complaintData) {
        super(complainer, complaintData);
        SystemManagerMember.removeComplaint(this);
    }

    @Override
    public void setCompAns(String compAns) {
        this.compAns = compAns;
    }

    @Override
    public String getCompAns() {
        return this.compAns;
    }
}
