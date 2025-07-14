package com.husyairi.ManaProgressAku.DTO.Activity;

public class DeleteActivityResponse {
    private  String activityID;

    public DeleteActivityResponse(String activityID) {
        this.activityID = activityID;
    }

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }
}
