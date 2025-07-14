package com.husyairi.ManaProgressAku.DTO.Session;

public class DeleteSessionResponse {

    private String sessionID;

    public DeleteSessionResponse(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
