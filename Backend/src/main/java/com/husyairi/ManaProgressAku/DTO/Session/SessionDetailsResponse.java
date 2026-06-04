package com.husyairi.ManaProgressAku.DTO.Session;

import com.husyairi.ManaProgressAku.DTO.Activity.ActivityDetailsResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SessionDetailsResponse {

    private String sesionID;
    private String status;
    private LocalDate date;
    private LocalTime time;
    private List<ActivityDetailsResponse> activities;

    public SessionDetailsResponse(String sesionID, String status, LocalDate date, LocalTime time, List<ActivityDetailsResponse> activities) {
        this.sesionID = sesionID;
        this.status = status;
        this.date = date;
        this.time = time;
        this.activities = activities;
    }

    public String getSesionID() {
        return sesionID;
    }

    public void setSesionID(String sesionID) {
        this.sesionID = sesionID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public List<ActivityDetailsResponse> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDetailsResponse> activities) {
        this.activities = activities;
    }
}
