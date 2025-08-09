package com.husyairi.ManaProgressAku.Service;

import com.husyairi.ManaProgressAku.DTO.Activity.GetActivityResponse;
import com.husyairi.ManaProgressAku.DTO.Activity.InsertActivityRequest;
import com.husyairi.ManaProgressAku.DTO.Activity.InsertActivityResponse;
import com.husyairi.ManaProgressAku.DTO.Activity.UpdateActivityRequest;
import com.husyairi.ManaProgressAku.Entity.Model.Activity;

import java.util.List;

public interface ActivityService {

    public InsertActivityResponse createActivity(InsertActivityRequest request);

    public GetActivityResponse getActivity(String sessionID);

    public Activity updateActivity(UpdateActivityRequest request);

    public void deleteActivity(String activityID);

    public int deleteActivitiesBySession(String sessionID);

    public List<Activity> getSessionActivities(String sessionID);

    public List<Activity> getAllActivities();

}
