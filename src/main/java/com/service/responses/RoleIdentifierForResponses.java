package com.service.responses;

import com.domain.logic.roles.IRole;

public class RoleIdentifierForResponses {

    String roleName;
    IRole role;

    public RoleIdentifierForResponses(IRole role) {
        this.role = role;
        roleName=findRoleName(role);
    }

    private String findRoleName(IRole roleToFind) {
        String fullRole=roleToFind.getClass().getName();
        String[] splittedClassName=fullRole.split(".");
        if (splittedClassName.length<1)
            return "role identification failed";
        return splittedClassName[splittedClassName.length-1];
    }

    public String getRoleName() {
        return roleName;
    }

    public IRole getRole() {
        return role;
    }
}
