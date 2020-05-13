package com.domain.logic.managers;

import com.domain.logic.roles.*;
import com.domain.logic.users.Member;
import com.domain.logic.users.SystemManagerMember;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageMembers {
    // static variable single_instance of type Singleton
    private static ManageMembers single_instance = null;

    private HashMap<String, Member> membersHashMap;

    // private constructor restricted to this class itself
    private ManageMembers()
    {
        membersHashMap = new HashMap<>();
    }

    // static method to create instance of Singleton class
    public static ManageMembers getInstance()
    {
        if (single_instance == null)
            single_instance = new ManageMembers();
        return single_instance;
    }

    /**
     * @param userName
     * @param hashPassword
     * @return the member using those properties
     */
    public Member findMember(String userName, String hashPassword){
        return membersHashMap.get(userName + hashPassword);
    }


    public Member findMember(Member member) {
        return membersHashMap.get(member.getUserName() + member.getPasswordHash());
    }


    public boolean removeMember(String userName, String passwordHash){
        Member removed = membersHashMap.remove(userName + passwordHash);
        if(removed != null)
            return true;
        return false;
    }

    /**
     * searching for members with the given name
     * @param name - member's name
     * @return list of members whose their name is the given name
     */
    public ArrayList<Member> findMemberByName(String name){
        ArrayList<Member> members = new ArrayList<>();
        for (Member member : membersHashMap.values()){
            if (member.getName().equalsIgnoreCase(name)){
                members.add(member);
            }
        }
        return members;
    }

    public ArrayList<Member> findMemberByUsername(String userName){
        ArrayList<Member> members = new ArrayList<>();
        for (Member member : membersHashMap.values()){
            if (member.getUserName().equalsIgnoreCase(userName))
                members.add(member);
        }
        return members;
    }

    /**
     * @param member
     * @return a boolean answer that represent if it possible to add the given member.
     * if true it adds the member to the members hash.
     */
    public boolean addMember(Member member){
        if(membersHashMap.containsKey(member.getUserName() + member.getPasswordHash())){
            return false;
        }
        membersHashMap.put(member.getUserName() + member.getPasswordHash(), member);
        return true;
    }


    /**
     * @return all the coaches in the system
     */
    public ArrayList<Coach> findAllCoaches() {
        ArrayList<Coach> allCoaches = new ArrayList<>();
        for (Member member : membersHashMap.values()){
            ArrayList<IRole> allTemp =  member.getAllTypeRole(Coach.class);
            for (IRole iRole : allTemp){
                allCoaches.add((Coach) iRole);
            }
        }
        return allCoaches;
    }


    /**
     * @return all the players in the system
     */
    public ArrayList<Player> findAllPlayers() {
        ArrayList<Player> allPlayers = new ArrayList<>();
        for (Member member : membersHashMap.values()){
            ArrayList<IRole> allTemp = member.getAllTypeRole(Player.class);
            for(IRole iRole : allTemp){
                allPlayers.add((Player) iRole);
            }
        }
        return allPlayers;
    }


    public ArrayList<Referee> findAllReferees() {
        ArrayList<Referee> allReferees = new ArrayList<>();
        for (Member member : membersHashMap.values()){
            ArrayList<IRole> allTemp = member.getAllTypeRole(Referee.class);
            for(IRole iRole : allTemp){
                allReferees.add((Referee) iRole);
            }
        }
        return allReferees;
    }

    public ArrayList<TeamOwner> findAllTeamOwner() {
        ArrayList<TeamOwner> allTeamOwners = new ArrayList<>();
        for (Member member : membersHashMap.values()){
            ArrayList<IRole> allTemp = member.getAllTypeRole(TeamOwner.class);
            for(IRole iRole : allTemp){
                allTeamOwners.add((TeamOwner) iRole);
            }
        }
        return allTeamOwners;
    }

    public ArrayList<AssociationAgent> findAllAssociationAgent() {
        ArrayList<AssociationAgent> allAssociationAgent = new ArrayList<>();
        for (Member member : membersHashMap.values()){
            ArrayList<IRole> allTemp = member.getAllTypeRole(AssociationAgent.class);
            for(IRole iRole : allTemp){
                allAssociationAgent.add((AssociationAgent) iRole);
            }
        }
        return allAssociationAgent;
    }

    public ArrayList<SystemManagerMember> findAllSystemManagers(){
        ArrayList<SystemManagerMember> systemManagerMembers = new ArrayList<>();
        for (Member member : membersHashMap.values()){
            if (member instanceof SystemManagerMember)
                systemManagerMembers.add((SystemManagerMember)member);
        }
        return systemManagerMembers;
    }

    public void removeAllMembers() {
        this.membersHashMap = new HashMap<>();
    }
}
