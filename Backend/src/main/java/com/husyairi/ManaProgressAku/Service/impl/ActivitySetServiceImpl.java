package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.ActivitySet.GetSetResponse;
import com.husyairi.ManaProgressAku.DTO.ActivitySet.InsertSetRequest;
import com.husyairi.ManaProgressAku.Entity.Model.Activity;
import com.husyairi.ManaProgressAku.Entity.Model.ActivitySet;
import com.husyairi.ManaProgressAku.Entity.Model.User;
import com.husyairi.ManaProgressAku.ExceptionHandling.BadRequestException;
import com.husyairi.ManaProgressAku.Repository.ActivityRepository;
import com.husyairi.ManaProgressAku.Repository.ActivitySetRepository;
import com.husyairi.ManaProgressAku.Repository.UserRepository;
import com.husyairi.ManaProgressAku.Service.ActivityService;
import com.husyairi.ManaProgressAku.Service.ActivitySetService;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ActivitySetServiceImpl implements ActivitySetService {

    @Autowired
    private ActivitySetRepository activitySetRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(ActivitySetServiceImpl.class);

    private Long getCurrentUserId(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        return currentUser.getId();
    }


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
                savedSet.getActivity().getActivityID()
        );
    }

    @Override
    public Long deleteSet (Long setID){

        ActivitySet set = activitySetRepository.findById(setID).
                orElseThrow(() -> new BadRequestException(
                        404,
                        "Activity Set not found",
                        new HashMap<>()
                ));

        if(!set.getActivity().getSession().getUserId().equals(getCurrentUserId())){
            throw new BadRequestException(403, "Not authorized to delete this set", new HashMap<>());
        }

        try{
            activitySetRepository.deleteById(setID);
        }catch (Exception e){
            throw new BadRequestException(500, e.getMessage(), new HashMap<>());
        }

        return setID;
    }

    public GetSetResponse getSet (Long setID){

        ActivitySet set = activitySetRepository.findById(setID).orElseThrow(()->
                new BadRequestException(404, "Activity set not found", new HashMap<>()));

        if(!set.getActivity().getSession().getUserId().equals(getCurrentUserId())){
            throw new BadRequestException(403, "Not authorized to view this set", new HashMap<>());
        }

        return new GetSetResponse(
                set.getSetID(),
                set.getWeight(),
                set.getReps(),
                set.getSetNumber(),
                set.getActivity().getActivityID()
        );
    }

    public GetSetResponse editSet (InsertSetRequest req, Long setID){

        ActivitySet fetchedSet = activitySetRepository.findById(setID).orElseThrow(() ->
                new BadRequestException(404, "Set not found", new HashMap<>()));

        if(!fetchedSet.getActivity().getSession().getUserId().equals(getCurrentUserId())){
            throw new BadRequestException(403, "Unauthorized to make this request", new HashMap<>());
        }

        fetchedSet.setSetNumber(req.getSetNumber());
        fetchedSet.setReps(req.getReps());
        fetchedSet.setWeight(req.getWeight());

        try{
            activitySetRepository.save(fetchedSet);
        }catch (Exception e){
            throw new BadRequestException(500, "Internal Server error", new HashMap<>());
        }

        return new GetSetResponse(
                fetchedSet.getSetID(),
                fetchedSet.getWeight(),
                fetchedSet.getReps(),
                fetchedSet.getSetNumber(),
                fetchedSet.getActivity().getActivityID()
        );

    }
}
