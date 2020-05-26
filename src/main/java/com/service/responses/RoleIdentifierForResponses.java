package com.service.responses;

import com.domain.logic.roles.IRole;

import java.util.regex.Pattern;

public class RoleIdentifierForResponses {

    String roleName;
    IRole role;

    public RoleIdentifierForResponses(IRole role) {
        this.role = role;
        roleName=findRoleName(role);
    }

    private String findRoleName(IRole roleToFind) {
        String fullRole=roleToFind.getClass().getName();
        String[] nameArr=fullRole.split(Pattern.quote("."));
        if (nameArr.length<1)
            return "no role name";
        return nameArr[nameArr.length-1];
    }

    public String getRoleName() {
        return roleName;
    }

    public IRole getRole() {
        return role;
    }
}
