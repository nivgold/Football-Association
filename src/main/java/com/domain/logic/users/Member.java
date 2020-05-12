package com.domain.logic.users;

import com.domain.logic.AssociationSystem;
import com.domain.logic.SearchSystem;
import com.domain.logic.data_types.Address;
import com.domain.logic.data_types.Complaint;
import com.domain.logic.data_types.PersonalPage;
import com.domain.logic.football.Game;
import com.domain.logic.roles.*;
import com.domain.logic.utils.SHA1Function;
import com.logger.Logger;

import java.util.ArrayList;

public class Member implements IGameObserver, IPersonalPageObserver {
    private Address address;
    private ArrayList<IRole> roles;
    private ArrayList<Complaint> unresolvedComplaints;
    private ArrayList<Complaint> resolvedComplaints;
    private String userName;
    private String email;
    private String name;
    private String passwordHash;


    public Member(String userName, String passwordHash, String email, Address address, String name) {
        this.userName = userName;
        this.name = name;
        this.passwordHash = passwordHash;
        this.email = email;
        this.address = address;
        this.unresolvedComplaints = new ArrayList<>();
        this.resolvedComplaints = new ArrayList<>();
        this.roles = new ArrayList<>();
        //TODO call DAO to add Member to the DB
        ManageMembers.getInstance().addMember(this);
    }

    public boolean logout(){
        if (!AssociationSystem.getInstance().checkIfConnected(this))
            return false;
        AssociationSystem.getInstance().logOutUser(this);
        Logger.getInstance().saveLog("Member logout successfully");
        return true;
    }

    /**
     * this method creates new unResolved complaint in the system
     * @param complaintData
     */
    public void writeComplaint(String complaintData){
        Complaint newComp = new Complaint(this, complaintData);
        Logger.getInstance().saveLog("the member -> " + this.userName + " sent new complaint to the system, compID:" + newComp.getCompID());
        this.unresolvedComplaints.add(newComp);
    }


    /**
     * this method returns the answer of the system manager for the complaint of that member
     * @param wantedCompId
     * @return string of relevant answer for the complaint
    ]
     */
    public String readResolvedComplaint(int wantedCompId) {
        for (int i = 0; i < this.resolvedComplaints.size(); i++) {
            if (this.resolvedComplaints.get(i).getCompID() == wantedCompId) {
                Logger.getInstance().saveLog("the member -> " + this.userName + " read the answer for his complaint, compId: " + wantedCompId);
                return this.resolvedComplaints.get(i).getCompAns();
            }
        }
        return "this complaint does not exists";
    }


    /**
     * returns all the same roles of this member. example: {Coach c1, Coach c2, Coach c3}
     * @param neededClass
     * @return
     */
    public ArrayList<IRole> getAllTypeRole(Class neededClass){
        ArrayList<IRole> result = new ArrayList<>();
        for (IRole role: roles){
            if (role.getClass()==neededClass)
                result.add(role);
        }
        return result;
    }


    /**
     * notify the member that his complaint has been answered
     * updating the complaints lists
     * @param complaint
     */
    public String notifyCompAns(Complaint complaint) {
        this.unresolvedComplaints.remove(complaint);
        this.resolvedComplaints.add(complaint);
        Logger.getInstance().saveLog("the member -> " + this.userName + " was notify that his complaint with comId: " + complaint.getCompID() + " has been answered");
        //notify the member that his complaint has been answered
        return  "Complaint " + complaint.getCompID() + " has been answered by System Manager";
    }


    /**
     * @param key
     * @param query
     * @return String of the wanted info
     */
    public String search(int key, String query){
        SearchSystem searcher = SearchSystem.getInstance();
        Logger.getInstance().saveLog("the member -> " + this.userName + " searched -> " + query);
        return searcher.search(key, query);
    }

    @Override
    public void updateGame(Game game) {
        System.out.println(game.toString());
    }

    @Override
    public void pageUpdate(PersonalPage personalPage) {
        System.out.println(personalPage.toString());
    }

    public boolean setUserName(String userName) {
        //TODO need to update Member username in the DB

        //TODO call DAO to remove user
        ManageMembers manageMembers = ManageMembers.getInstance();
        boolean removal = manageMembers.removeMember(this.userName, this.passwordHash);
        if(removal) {
            this.userName = userName;
            //TODO call DAO to add the Member
            if(manageMembers.addMember(this)){
                Logger.getInstance().saveLog("the member -> " + this.name + " changed his userName to -> " + userName);
                return true;
            }
        }
        return false;
    }

    public boolean setPasswordHash(String password) {
        //TODO need to update Member password in the DB
        ManageMembers manageMembers = ManageMembers.getInstance();
        boolean removal = manageMembers.removeMember(this.userName, this.passwordHash);
        if(removal){
            String hashPassword = SHA1Function.hash(password);
            this.passwordHash = hashPassword;
            if(manageMembers.addMember(this)){
                Logger.getInstance().saveLog("the member -> " + this.userName + " changed his password");
                return true;
            }
        }
        return false;
    }

    public boolean removeYourself() throws Exception {
        for (IRole role: this.roles) {
            if(!role.removeYourself())
                return false;
        }
        for(Complaint complaint : this.unresolvedComplaints) {
            if(!complaint.removeYourself())
                return false;
        }
        //TODO call DAO to remove Member from DB
        ManageMembers manageMembers = ManageMembers.getInstance();
        Logger.getInstance().saveLog("the member -> " + this.userName + " was deleted from the system");
        return manageMembers.removeMember(this.userName, this.passwordHash);
    }

    public void registerToPersonalPage(PersonalPage personalPage){
        personalPage.register(this);
    }

    public void removeRegistrationFromPersonalPage(PersonalPage personalPage){
        personalPage.remove(this);
    }

    public void registerToGame(Game game){
        game.register(this);
    }

    public void removeRegistrationFromGame(Game game){
        game.remove(this);
    }

    public String getUserName() {
        return userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Address getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public ArrayList<Complaint> getUnresolvedComplaints() {
        return unresolvedComplaints;
    }

    public ArrayList<Complaint> getResolvedComplaints() {
        return resolvedComplaints;
    }

    //TODO fix the adding roles func
    public ArrayList<IRole> getRoles() {
        if(!(this instanceof SystemManagerMember))
            return roles;
        else{
            System.err.println("system manager doesn't have roles!");
            return null;
        }
    }

    public boolean removeTeamOwner(TeamOwner teamOwner) throws Exception {
        if(!this.roles.remove(teamOwner)){
            String excepData = "the member: " + this.getUserName() + " is not the team owner of the team: " + teamOwner.getTeam().getTeamName();
            throw new Exception(excepData);
        }
        return true;
    }

    public boolean removeTeamManager(TeamManager teamManager) throws Exception {
        if(!this.roles.remove(teamManager)){
            String excepData = "the member: " + this.getUserName() + " is not the team manager of the team: " + teamManager.getTeam().getTeamName();
            throw new Exception(excepData);
        }
        return true;
    }

    public boolean addAssociationAgent(AssociationAgent associationAgent) throws Exception {
        if(checkRoleAddition(associationAgent)){
            this.roles.add(associationAgent);
            return true;
        }
        else{
            String excepData = "the member: " + this.getUserName() + " can't be assigned as association agent ";
            throw new Exception(excepData);
        }
    }

    public boolean removeAssociationAgent(AssociationAgent associationAgent) throws Exception {
        if(!this.roles.remove(associationAgent)){
            String excepData = "the member: " + this.getUserName() + " is not an association agent";
            throw new Exception(excepData);
        }
        return true;
    }

    public boolean addChoach(Coach coach) throws Exception {
        if(checkRoleAddition(coach)){
            this.roles.add(coach);
            return true;
        }
        else{
            String excepData = "the member: " + this.getUserName() + " can't be assigned as coach ";
            throw new Exception(excepData);
        }
    }

    public boolean removeCoach(Coach coach) throws Exception {
        if(!this.roles.remove(coach)){
            String excepData = "the member: " + this.getUserName() + " is not a coach";
            throw new Exception(excepData);
        }
        return true;
    }

    public boolean addPlayer(Player player) throws Exception {
        if(checkRoleAddition(player)){
            this.roles.add(player);
            return true;
        }
        else{
            String excepData = "the member: " + this.getUserName() + " can't be assigned as player ";
            throw new Exception(excepData);
        }
    }

    public boolean removePlayer(Player player) throws Exception {
        if(!this.roles.remove(player)){
            String excepData = "the member: " + this.getUserName() + " is not the player: " + player.getPlayerID();
            throw new Exception(excepData);
        }
        return true;
    }

    public boolean addReferee(Referee referee) throws Exception {
        if(checkRoleAddition(referee)){
            this.roles.add(referee);
            return true;
        }
        else{
            String excepData = "the member: " + this.getUserName() + " can't be assigned as referee";
            throw new Exception(excepData);
        }
    }

    public boolean removeReferee(Referee referee) throws Exception {
        if(!this.roles.remove(referee)){
            String excepData = "the member: " + this.getUserName() + " is not a referee";
            throw new Exception(excepData);
        }
        return true;
    }

    public boolean addTeamManager(TeamManager teamManager) throws Exception {
        if(checkRoleAddition(teamManager)){
            this.roles.add(teamManager);
            return true;
        }
        else{
            String excepData = "the member: " + this.getUserName() + " can't be assigned as team manager";
            throw new Exception(excepData);
        }
    }

    public boolean addTeamOwner(TeamOwner teamOwner) throws Exception {
        if(checkRoleAddition(teamOwner)){
            this.roles.add(teamOwner);
            return true;
        }
        else{
            String excepData = "the member: " + this.getUserName() + " can't be assigned as team owner";
            throw new Exception(excepData);
        }
    }

    private boolean checkRoleAddition(IRole role) {
        if(role instanceof AssociationAgent || role instanceof Referee) {
            if (this.roles.size() == 0) {
                return true;
            } else {
                return false;
            }
        }
        else{
            for (int i = 0; i < this.roles.size(); i++) {
                IRole currentRole = this.roles.get(i);
                if(currentRole instanceof AssociationAgent || currentRole instanceof Referee ||
                    currentRole.getClass() == role.getClass()){
                    return false;
                }
            }
            return true;
        }
    }
}

