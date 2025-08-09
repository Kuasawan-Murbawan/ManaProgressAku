package com.husyairi.ManaProgressAku.DTO.Activity;

public class DeleteActivitiesBySessionResponse {
    private String sessionID;
    private int deletedCount;

    public DeleteActivitiesBySessionResponse(String sessionID, int deletedCount) {
        this.sessionID = sessionID;
        this.deletedCount = deletedCount;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public int getDeletedCount() {
        return deletedCount;
    }

    public void setDeletedCount(int deletedCount) {
        this.deletedCount = deletedCount;
    }
}
