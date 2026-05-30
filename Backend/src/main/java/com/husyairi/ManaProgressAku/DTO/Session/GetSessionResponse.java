package com.husyairi.ManaProgressAku.DTO.Session;

import java.time.LocalDate;
import java.time.LocalTime;

public class GetSessionResponse {
    private String sessionID;

    private LocalTime time;

    private LocalDate date;

    private String status;

    public GetSessionResponse() {
    }

    public GetSessionResponse(String sessionID, LocalTime time, LocalDate date, String status) {
        this.sessionID = sessionID;
        this.time = time;
        this.date = date;
        this.status = status;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
