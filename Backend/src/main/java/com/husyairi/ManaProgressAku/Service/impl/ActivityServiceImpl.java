package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.Activity.GetActivityResponse;
import com.husyairi.ManaProgressAku.DTO.Activity.InsertActivityRequest;
import com.husyairi.ManaProgressAku.DTO.Activity.InsertActivityResponse;
import com.husyairi.ManaProgressAku.DTO.Activity.UpdateActivityRequest;
import com.husyairi.ManaProgressAku.Entity.Model.Activity;
import com.husyairi.ManaProgressAku.Service.ActivityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Override
    public InsertActivityResponse createActivity(InsertActivityRequest request){
        return null;
    };

    @Override
    public GetActivityResponse getActivity(String sessionID){
        return null;
    };

    @Override
    public Activity updateActivity(UpdateActivityRequest request){
        return null;
    };

    @Override
    public void deleteActivity(String activityID){

    };

    @Override
    public List<Activity> getAllActivities(){
        return null;
    };

}
