package com.husyairi.ManaProgressAku.DTO.Session;

public class InsertSessionResponse {

    private String sessionID ;

    public InsertSessionResponse( String sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
