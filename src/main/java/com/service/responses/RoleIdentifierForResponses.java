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
        return roleToFind.getClass().getName();
    }

    public String getRoleName() {
        return roleName;
    }

    public IRole getRole() {
        return role;
    }
}
