package com.husyairi.ManaProgressAku.DTO.ActivitySet;

import com.husyairi.ManaProgressAku.Entity.Model.ActivitySet;

import java.util.List;

public class EditActivitySetRequest {

    String activityID;

    List<ActivitySet> activitySetList;

    public EditActivitySetRequest() {
    }

    public EditActivitySetRequest(String activityID, List<ActivitySet> activitySetList) {
        this.activityID = activityID;
        this.activitySetList = activitySetList;
    }

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    public List<ActivitySet> getActivitySetList() {
        return activitySetList;
    }

    public void setActivitySetList(List<ActivitySet> activitySetList) {
        this.activitySetList = activitySetList;
    }
}
