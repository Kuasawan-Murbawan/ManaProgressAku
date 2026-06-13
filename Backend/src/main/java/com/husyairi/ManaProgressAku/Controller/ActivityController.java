package com.husyairi.ManaProgressAku.Controller;

import com.husyairi.ManaProgressAku.DTO.Activity.*;
import com.husyairi.ManaProgressAku.DTO.Session.DeleteSessionResponse;
import com.husyairi.ManaProgressAku.Entity.Model.Activity;
import com.husyairi.ManaProgressAku.ExceptionHandling.ApiSuccessResponse;
import com.husyairi.ManaProgressAku.Service.ActivityService;
import com.husyairi.ManaProgressAku.Service.ActivitySetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Activity", description = "CRUD for activity")
@CrossOrigin("*")
@RestController
public class ActivityController {

    private final ActivityService activityService;

    private final ActivitySetService activitySetService;

    @Autowired
    public ActivityController(ActivityService activityService, ActivitySetService activitySetService) {
        this.activityService = activityService;
        this.activitySetService = activitySetService;
    }


    @Operation(
            summary = "Insert new Activity",
            description = "Create new Activity using Activity ID, weight, set, reps"
    )
    @PostMapping("/insertActivity")
    public ResponseEntity<ApiSuccessResponse<InsertActivityResponse>> insertActivity(@RequestBody InsertActivityRequest request){
        InsertActivityResponse data = activityService.createActivity(request);

        ApiSuccessResponse<InsertActivityResponse> response = new ApiSuccessResponse<>(
                "Activity created successfully",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Deprecated
    @Operation(
            summary = "Get an Activity",
            description = "Return an Activity using Activity ID"
    )
    @GetMapping("/getActivity/{activityID}")
    public ResponseEntity<ApiSuccessResponse<GetActivityResponse>> getActivity(@PathVariable String activityID){
        GetActivityResponse data = activityService.getActivity(activityID);

        ApiSuccessResponse<GetActivityResponse> response = new ApiSuccessResponse<>(
                "Activity fetched successfully",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Deprecated
    @Operation(
            summary = "Update Activity",
            description = "Update an Activity using all Activity details in request body"
    )
    @PutMapping("/updateActivity")
    public ResponseEntity<ApiSuccessResponse<Activity>> updateActivity(@RequestBody UpdateActivityRequest request){

        Activity data = activityService.updateActivity(request);

        ApiSuccessResponse<Activity> response = new ApiSuccessResponse<>(
                "Activity Updated",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Activity",
            description = "Delete Activity using Activity ID"
    )
    @DeleteMapping("/deleteActivity/{activityID}")
    public ResponseEntity<ApiSuccessResponse<DeleteActivityResponse>> deleteActivity(@PathVariable String activityID){

        activitySetService.deleteAllSetByActivity(activityID);
        activityService.deleteActivity(activityID);

        DeleteActivityResponse response = new DeleteActivityResponse(activityID);

        return ResponseEntity.ok(
                new ApiSuccessResponse<>(
                        "Activity deleted successfully!",
                        response
                )
        );
    }

    @Operation(
            summary = "Delete Activities in a Session",
            description = "Delete all Activities in a Session using Session ID"
    )
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

    @Operation(
            summary = "Get all Activities in a Session",
            description = "Fetch all Activities in a Session using Session ID"
    )
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
