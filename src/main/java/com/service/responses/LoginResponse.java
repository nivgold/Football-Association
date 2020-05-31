package com.service.responses;

import com.domain.logic.roles.IRole;
import com.domain.logic.users.Member;

import java.util.ArrayList;

public class LoginResponse {

    private Member member;
    private ArrayList<RoleIdentifierForResponses> roles;
    private ArrayList<Integer> subscribedGames;
    private String status;


    public LoginResponse(Member member, String status) {
        this.status = status;
        this.member = member;
        this.subscribedGames = new ArrayList<>();
        if (member!=null) {
            roles = fillRolesList(member.getRoles());
            subscribedGames.addAll(member.getObservedGameIDs());
        }
    }

    private ArrayList<RoleIdentifierForResponses> fillRolesList(ArrayList<IRole> originalRoles) {
        ArrayList<RoleIdentifierForResponses> output = new ArrayList<>();
        for (IRole role:originalRoles) {
         output.add(new RoleIdentifierForResponses(role));
        }
        return output;
    }

    public String getStatus() {
        return status;
    }

    public Member getMember() {
        return member;
    }

    public ArrayList<RoleIdentifierForResponses> getRoles() {
        return roles;
    }

    public ArrayList<Integer> getSubscribedGames() {
        return subscribedGames;
    }
}
