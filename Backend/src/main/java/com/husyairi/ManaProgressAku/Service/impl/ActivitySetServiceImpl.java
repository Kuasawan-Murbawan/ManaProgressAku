package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.ActivitySet.GetSetResponse;
import com.husyairi.ManaProgressAku.DTO.ActivitySet.InsertSetRequest;
import com.husyairi.ManaProgressAku.Entity.Model.Activity;
import com.husyairi.ManaProgressAku.Entity.Model.ActivitySet;
import com.husyairi.ManaProgressAku.ExceptionHandling.BadRequestException;
import com.husyairi.ManaProgressAku.Repository.ActivityRepository;
import com.husyairi.ManaProgressAku.Repository.ActivitySetRepository;
import com.husyairi.ManaProgressAku.Service.ActivityService;
import com.husyairi.ManaProgressAku.Service.ActivitySetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ActivitySetServiceImpl implements ActivitySetService {

    @Autowired
    private ActivitySetRepository activitySetRepository;

    @Autowired
    private ActivityRepository activityRepository;


    @Override
    public GetSetResponse insertSet (InsertSetRequest req){

        Activity currentActivity = activityRepository.findById(req.getActivityID())
                .orElseThrow(() -> new BadRequestException(404, "Activity not found", new HashMap<>()));

        ActivitySet newSet = new ActivitySet(
                req.getWeight(),
                req.getReps(),
                req.getSetNumber(),
                currentActivity
        );

        ActivitySet savedSet = activitySetRepository.save(newSet);

        return new GetSetResponse(
                savedSet.getSetID(),
                savedSet.getWeight(),
                savedSet.getReps(),
                savedSet.getSetNumber(),
                savedSet.getActivity()
        );
    }

}
