package com.husyairi.ManaProgressAku.DTO.Activity;

public class InsertActivityResponse {

    private String status;
    private String message;
    private String activityID;

    public InsertActivityResponse(String status, String message, String activityID) {
        this.status = status;
        this.message = message;
        this.activityID = activityID;
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

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }
}
