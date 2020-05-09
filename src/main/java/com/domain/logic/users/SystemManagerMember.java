package com.domain.logic.users;

import com.domain.logic.RecommenderSystem;
import com.domain.logic.recommender_system_strategies.IRecommenderSystemStrategy;
import com.domain.logic.data_types.Address;
import com.domain.logic.data_types.Complaint;
import com.domain.logic.football.Team;
import com.domain.logic.utils.SHA1Function;
import com.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SystemManagerMember extends Member{
    private static int managerCount = 0;
    private static Logger logger = Logger.getInstance();
    private RecommenderSystem recommenderSystem;
    private String userName;
    private String passwordHash;

    //static hash that contains all the complaints that need to be review by the system manager
    private static HashMap<Integer, Complaint> unResolvedComplaints = new HashMap<>();


    public SystemManagerMember(String userName, String password, String email, Address address, String name) {
        super(userName, SHA1Function.hash(password), email, address, name);
        this.passwordHash = SHA1Function.hash(password);
        this.userName = userName;
        //TODO call DAO to add SystemMember to the DB
        ManageMembers.getInstance().addMember(this);
        managerCount++;
    }

    public SystemManagerMember(Member member) {
        //TODO call DAO to add SystemMember to the DB
        super(member.getUserName(), member.getPasswordHash(), member.getEmail(), member.getAddress(), member.getName());
        ManageMembers.getInstance().addMember(this);
        managerCount++;
    }

    /**
     * adding new unresolved complaint
     * @param complaint
     */
    public static void addComplaint(Complaint complaint) {
        Logger.getInstance().saveLog("new complaint was added to the system -> " + complaint.getCompID());
        unResolvedComplaints.put(complaint.getCompID(), complaint);
    }


    /**
     * removing unresolved complaint
     * @param complaint
     * @return
     */
    public static boolean removeComplaint(Complaint complaint) {
        Logger.getInstance().saveLog("complaint was removed from the system -> " + complaint.getCompID());
        Complaint comp = unResolvedComplaints.remove(complaint.getCompID());
        if(comp != null)
            return true;
        return false;
    }


    /**
     * removing unresolved complaint
     * @param complaintId
     * @return
     */
    public static boolean removeComplaint(int complaintId) {
        Logger.getInstance().saveLog("complaint was removed from the system -> " + complaintId);
        Complaint comp = unResolvedComplaints.remove(complaintId);
        if(comp != null)
            return true;
        return false;
    }


    /**
     * @return arrayList of strings of all the complaints data
     */
    public ArrayList<String> readUnhandledComplaints(){
        ArrayList<String> compsData = new ArrayList<>();
        Iterator hmIterator = unResolvedComplaints.entrySet().iterator();
        //iterate through all complaints, takes their's data
        while (hmIterator.hasNext()){
            Map.Entry element = (Map.Entry)hmIterator.next();
            compsData.add(((Complaint)element.getValue()).getComplaintData());
        }
        return compsData;
    }

    /**
     * answer given complaints and removes them from the unhandled hash of complaints
     * @param compIds
     * @param compAnswers
     * @return a boolean that represent if all the wanted complaints have been answered
     */
    public boolean answerComplaints(int [] compIds, String [] compAnswers){
        for (int i = 0; i < compIds.length; i++) {
            Complaint currentComp = unResolvedComplaints.get(compIds[i]);
            if(currentComp == null)
                return false;
            else{
                currentComp.setCompAns(compAnswers[i]);
                //removes handled complaints
                unResolvedComplaints.remove(compIds[i]);
            }
        }
        return true;
    }

    public SystemManagerMember appointSystemManager(Member member){
        if(member instanceof SystemManagerMember)
            return null;
        //TODO call DAO to remove Member from DB
        if(ManageMembers.getInstance().removeMember(member.getUserName(), member.getPasswordHash())) {
            SystemManagerMember appoint = new SystemManagerMember(member);
            Logger.getInstance().saveLog("new system manager was added to the system -> " + member.getUserName() + " by -> " + this.userName);
            return appoint;
        }
        else
            return null;
    }

    public boolean deleteMember(Member member){
        if(member instanceof  SystemManagerMember) {
            if(managerCount > 1){
                if(member.removeYourself()) {
                    Logger.getInstance().saveLog("the system manager: " + this.userName + " removed the member: " + member.getUserName());
                    managerCount--;
                    return true;
                }
            }
            return false;
        }
        else {
            Logger.getInstance().saveLog("the system manager: " + this.userName + " tried to remove the member: " + member.getUserName());
            return member.removeYourself();
        }
    }

    public boolean closeTeamPermanently(Team team){
        Logger.getInstance().saveLog("the system manager: " + this.userName + " tried to remove the team: " + team.getTeamName());
        return team.removeTeamPermanently();
    }

    public void watchSystemLogger(){
        System.out.println(logger.watchLogFile());
    }

    public boolean activateRecommenderSystem(IRecommenderSystemStrategy recommenderSystemStrategy){
        Logger.getInstance().saveLog("the system manager: " + this.userName + " built new recommender system");
        recommenderSystem = RecommenderSystem.getInstance();
        recommenderSystem.BuildRecommenderSystem(recommenderSystemStrategy);
        return true;
    }
}
