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
                .orElseThrow(() -> new RuntimeException("Session not found"));

        Activity newActivity = new Activity(
                generateActivityID(),
                userSession,
                request.getExerciseID(),
                request.getSets(),
                request.getRep(),
                request.getWeight()
        );

        try{
            Activity savedActivity = activityRepository.save(newActivity);
            return new InsertActivityResponse(
                    savedActivity.getActivityID(),
                    savedActivity.getSession(),
                    savedActivity.getExerciseID(),
                    savedActivity.getSets(),
                    savedActivity.getRep(),
                    savedActivity.getWeight()
            );
        }catch (Exception e){
            throw new BadRequestException(400, "Error saving activity: " + e.getMessage(), new HashMap<>());
        }
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

        Optional<Activity> isExist = activityRepository.findById(request.getActivityID());

        if(isExist.isEmpty()){
            throw new BadRequestException(404, "Session ID : " + request.getActivityID() + " not found." , new HashMap<>());
        }

        Activity updatedActivity = isExist.get();
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
        boolean isExist = activityRepository.existsById(activityID);

        if(!isExist){
            throw new BadRequestException(404, "Activity ID : " + activityID + " not found", new HashMap<>());
        }

        try{
            activityRepository.deleteById(activityID);
        }catch (Exception e){
            throw new BadRequestException(500, e.getMessage(), new HashMap<>());
        }
    }

    @Override
    public int deleteActivitiesBySession(String sessionID){
        List<Activity> activities = activityRepository.findBySession_SessionID(sessionID);

        if(activities.isEmpty()){
            // we put 0 instead of throwing Exception bc it is not an error
            // it is possible for session to have 0 activities (if the cancel)
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
        Optional<Session> isExist = sessionRepository.findById(sessionID);

        if(isExist.isEmpty()){
            throw new BadRequestException(404, "Session ID not found", new HashMap<>());
        }

        // extract the userid
        Long sessionUserId = isExist.get().getUserId();

        // compare with the current user id
        if(sessionUserId == null || !sessionUserId.equals(getCurrentUserId())){
            throw new BadRequestException(403, "Not Authorized", new HashMap<>());
        }

        return activityRepository.findBySession_SessionID(sessionID);
    }

    @Override
    public List<Activity> getAllActivities(){
        return activityRepository.findAll();
    }

}
