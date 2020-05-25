package com.service.request_data_holders;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {

    String userName;
    String password;
    String firstName;
    String lastName;

    public LoginRequest(@JsonProperty("userName") String userName, @JsonProperty("password") String password, @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
