package com.service.request_data_holders;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResetSystemRequest {

    String sysManagerUserName;

    public ResetSystemRequest(@JsonProperty("sysManagerUserName") String sysManagerUserName) {
        this.sysManagerUserName = sysManagerUserName;
    }

    public String getSysManagerUserName() {
        return sysManagerUserName;
    }
}
