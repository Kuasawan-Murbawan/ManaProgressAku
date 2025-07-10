package com.husyairi.ManaProgressAku.DTO.Session;

public class InsertSessionResponse {
    private String status;

    private String sessionID ;

    private String message;

    public InsertSessionResponse(String status, String message, String sessionID) {
        this.status = status;
        this.message = message;
        this.sessionID = sessionID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
