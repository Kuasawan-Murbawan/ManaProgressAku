package com.husyairi.ManaProgressAku.Controller;

import com.husyairi.ManaProgressAku.DTO.Activity.*;
import com.husyairi.ManaProgressAku.DTO.Session.DeleteSessionResponse;
import com.husyairi.ManaProgressAku.Entity.Model.Activity;
import com.husyairi.ManaProgressAku.ExceptionHandling.ApiSuccessResponse;
import com.husyairi.ManaProgressAku.Service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }


    @PostMapping("/insertActivity")
    public ResponseEntity<ApiSuccessResponse<InsertActivityResponse>> insertActivity(@RequestBody InsertActivityRequest request){
        InsertActivityResponse data = activityService.createActivity(request);

        ApiSuccessResponse<InsertActivityResponse> response = new ApiSuccessResponse<>(
                "Activity created successfully",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/getActivity/{activityID}")
    public ResponseEntity<ApiSuccessResponse<GetActivityResponse>> getActivity(@PathVariable String activityID){
        GetActivityResponse data = activityService.getActivity(activityID);

        ApiSuccessResponse<GetActivityResponse> response = new ApiSuccessResponse<>(
                "Activity fetched successfully",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping("/updateActivity")
    public ResponseEntity<ApiSuccessResponse<Activity>> updateActivity(@RequestBody UpdateActivityRequest request){

        Activity data = activityService.updateActivity(request);

        ApiSuccessResponse<Activity> response = new ApiSuccessResponse<>(
                "Activity Updated",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/deleteActivity/{activityID}")
    public ResponseEntity<ApiSuccessResponse<DeleteActivityResponse>> deleteActivity(@PathVariable String activityID){

        activityService.deleteActivity(activityID);

        DeleteActivityResponse response = new DeleteActivityResponse(activityID);

        return ResponseEntity.ok(
                new ApiSuccessResponse<>(
                        "Activity deleted successfully!",
                        response
                )
        );
    }

    @DeleteMapping("/deleteActivitiesBySessionID/{sessionID}")
    public ResponseEntity<ApiSuccessResponse<DeleteActivitiesBySessionResponse>> deleteActivitiesBySessionID(@PathVariable String sessionID){
        int deletedCount = activityService.deleteActivitiesBySession(sessionID);

        DeleteActivitiesBySessionResponse response = new DeleteActivitiesBySessionResponse(sessionID, deletedCount);

        return ResponseEntity.ok(
                new ApiSuccessResponse<>(
                        "Activities deleted successfully!",
                        response
                )
        );
    }

    @GetMapping("/sessionActivities/{sessionID}")
    public ResponseEntity<ApiSuccessResponse<List<Activity>>> getSessionActivities(@PathVariable String sessionID){


        List<Activity> data =  activityService.getSessionActivities(sessionID);

        ApiSuccessResponse<List<Activity>> response = new ApiSuccessResponse<>(
                "Activities fetched successfully for session :" + sessionID,
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/admin/getAllActivity")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiSuccessResponse<List<Activity>>> getAllActivities(){

        List<Activity> data =  activityService.getAllActivities();

        ApiSuccessResponse<List<Activity>> response = new ApiSuccessResponse<>(
                "Activities fetched successfully" ,
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminTest() {
        return "You are admin!";
    }

}
