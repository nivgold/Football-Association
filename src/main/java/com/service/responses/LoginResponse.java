package com.service.responses;

import com.domain.logic.users.Member;

public class LoginResponse {

    private Member member;
    private String status;

    public LoginResponse(Member member, String status) {
        this.status = status;
        this.member = member;
    }

    public String getStatus() {
        return status;
    }

    public Member getMember() {
        return member;
    }
}
