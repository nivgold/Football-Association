package com.domain.logic.data_types;


import com.domain.logic.users.Member;
import com.domain.logic.users.SystemManagerMember;

public class Complaint {
    protected int compID;
    protected Member complainer;
    protected String complaintData;
    protected String compAns;
    protected static int compCount = 0;

    public Complaint(Member complainer, String complaintData) {
        this.complainer = complainer;
        this.complaintData = complaintData;
        this.compID = compCount;
        compCount++;
        //adding the new complaint to the manager's hash of complaints
        SystemManagerMember.addComplaint(this);
    }

    public String getComplaintData() {
        return complaintData;
    }

    public void setCompAns(String compAns) {
        this.compAns = compAns;
        this.complainer.notifyCompAns(this);
    }

    public String getCompAns() {
        return compAns;
    }

    public int getCompID() {
        return compID;
    }

    public boolean removeYourself() {
        return SystemManagerMember.removeComplaint(this);
    }
}
