package com.husyairi.ManaProgressAku.DTO.Session;

import java.time.LocalDate;

public class ActiveSessionResponse {

    private boolean hasActiveSession;

    private String sessionID;

    private LocalDate startedAt;

    private String status;

    public ActiveSessionResponse() {
    }

    public ActiveSessionResponse(boolean hasActiveSession, String sessionID, LocalDate startedAt, String status) {
        this.hasActiveSession = hasActiveSession;
        this.sessionID = sessionID;
        this.startedAt = startedAt;
        this.status = status;
    }

    public boolean isHasActiveSession() {
        return hasActiveSession;
    }

    public void setHasActiveSession(boolean hasActiveSession) {
        this.hasActiveSession = hasActiveSession;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public LocalDate getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDate startedAt) {
        this.startedAt = startedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
