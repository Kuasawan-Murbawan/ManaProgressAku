package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.Activity.GetActivityResponse;
import com.husyairi.ManaProgressAku.DTO.Activity.InsertActivityRequest;
import com.husyairi.ManaProgressAku.DTO.Activity.InsertActivityResponse;
import com.husyairi.ManaProgressAku.DTO.Activity.UpdateActivityRequest;
import com.husyairi.ManaProgressAku.Entity.Model.Activity;
import com.husyairi.ManaProgressAku.Entity.Model.Session;
import com.husyairi.ManaProgressAku.Entity.Model.User;
import com.husyairi.ManaProgressAku.ExceptionHandling.BadRequestException;
import com.husyairi.ManaProgressAku.Repository.ActivityRepository;
import com.husyairi.ManaProgressAku.Repository.SessionRepository;
import com.husyairi.ManaProgressAku.Repository.UserRepository;
import com.husyairi.ManaProgressAku.Service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    private Long getCurrentUserId() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                "User not found"
        ));

        return currentUser.getId();
    }

    private String generateActivityID(){

        Activity latestActivity = activityRepository.findTopByOrderByActivityIDDesc();

        String newActivityID;

        if(latestActivity == null){
            newActivityID = "ACT001";
        }else{
            String lastID = latestActivity.getActivityID(); // eg. "ACT005"
            int num = Integer.parseInt(lastID.substring(4)); // get 005
            newActivityID = String.format("ACT%03d", num + 1);
        }

        return newActivityID;
    }

    @Override
    public InsertActivityResponse createActivity(InsertActivityRequest request){

        Session userSession = sessionRepository.findById(request.getSessionID())
                .orElseThrow(() -> new BadRequestException(404 , "Session not found", new HashMap<>()));

        if(!userSession.getUserId().equals(getCurrentUserId())){
            throw new BadRequestException(403, "Not authorized to add this activity to this session.", new HashMap<>());
        }

        Activity newActivity = new Activity(
                generateActivityID(),
                userSession,
                request.getExerciseID(),
                request.getSets(),
                request.getRep(),
                request.getWeight()
        );

        Activity savedActivity = activityRepository.save(newActivity);

        return new InsertActivityResponse(
                savedActivity.getActivityID(),
                savedActivity.getSession(),
                savedActivity.getExerciseID(),
                savedActivity.getSets(),
                savedActivity.getRep(),
                savedActivity.getWeight());

    }

    @Override
    public GetActivityResponse getActivity(String activityID){

        Activity activity = activityRepository.findById(activityID)
                .orElseThrow(() ->
                        new BadRequestException(404,
                                "Activity ID: " + activityID + " not found",
                                new HashMap<>()));

        Long currentUserId = getCurrentUserId();

        if(!activity.getSession().getUserId().equals(currentUserId)){
            throw new BadRequestException(403, "Not authorized", new HashMap<>());
        }

        return new GetActivityResponse(
                activity.getActivityID(),
                activity.getSession().getSessionID(), // just ID, not full Session
                activity.getExerciseID(),
                activity.getSets(),
                activity.getWeight(),
                activity.getRep()
        );

    }

    @Override
    public Activity updateActivity(UpdateActivityRequest request){

        Activity updatedActivity = activityRepository.findById(request.getActivityID()).
                orElseThrow(() -> new BadRequestException(404, "Activity ID " + request.getActivityID() + " not found", new HashMap<>()));

        if(!updatedActivity.getSession().getUserId().equals(getCurrentUserId())){
            throw new BadRequestException(403, "Not authorized to access this session", new HashMap<>());
        }

        updatedActivity.setActivityID(request.getActivityID());
        updatedActivity.setExerciseID(request.getExerciseID());
        updatedActivity.setSets(request.getSets());
        updatedActivity.setWeight(request.getWeight());
        updatedActivity.setRep(request.getRep());

        try{
            activityRepository.save(updatedActivity);
        }catch (Exception e){
            throw new BadRequestException(500, "", new HashMap<>());
        }

        return updatedActivity;
    }

    @Override
    public void deleteActivity(String activityID){

        Activity activity = activityRepository.findById(activityID).
                orElseThrow(() -> new BadRequestException(404, "Activity not found", new HashMap<>()));

        if(!activity.getSession().getUserId().equals(getCurrentUserId())){
            throw new BadRequestException(403, "Not authorized to delete activity", new HashMap<>());
        }

        try{
            activityRepository.deleteById(activity.getActivityID());
        }catch (Exception e){
            throw new BadRequestException(500, e.getMessage(), new HashMap<>());
        }
    }

    @Override
    public int deleteActivitiesBySession(String sessionID){
        List<Activity> activities = activityRepository.findBySession_SessionID(sessionID);

        Session session = sessionRepository.findById(sessionID).orElseThrow(() -> new BadRequestException(
                404, "No session found", new HashMap<>()
        ));

        if(!session.getUserId().equals(getCurrentUserId())){
            throw new BadRequestException(403, "Not authorized to delete activities", new HashMap<>());
        }

        if(activities.isEmpty()){
            // we put 0 instead of throwing Exception bc it is not an error
            // it is possible for session to have 0 activities (if they cancel)
            return 0;
        }

        try{
            activityRepository.deleteAll(activities);
            return activities.size();
        }catch (Exception e){
            throw new BadRequestException(500, e.getMessage(), new HashMap<>());
        }
    }

    @Override
    public List<Activity> getSessionActivities(String sessionID){

        // get session object
        Session session = sessionRepository.findById(sessionID).
                orElseThrow(() -> new BadRequestException(404, "No session found", new HashMap<>() ));

        if(!session.getUserId().equals(getCurrentUserId())){
            throw new BadRequestException(403, "Not authorized to fetch this session", new HashMap<>());
        }

        return activityRepository.findBySession_SessionID(sessionID);
    }

    @Override
    public List<Activity> getAllActivities(){

        return activityRepository.findAll();
    }

}
