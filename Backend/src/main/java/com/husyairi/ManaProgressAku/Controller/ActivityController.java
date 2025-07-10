package com.husyairi.ManaProgressAku.Controller;

import com.husyairi.ManaProgressAku.DTO.Activity.GetActivityResponse;
import com.husyairi.ManaProgressAku.DTO.Activity.InsertActivityRequest;
import com.husyairi.ManaProgressAku.DTO.Activity.InsertActivityResponse;
import com.husyairi.ManaProgressAku.DTO.Activity.UpdateActivityRequest;
import com.husyairi.ManaProgressAku.Entity.Model.Activity;
import com.husyairi.ManaProgressAku.ExceptionHandling.ApiSuccessResponse;
import com.husyairi.ManaProgressAku.Service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/mana-progress-aku")
public class ActivityController {

    private ActivityService activityService;

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
    public ResponseEntity<ApiSuccessResponse<String>> deleteActivity(@PathVariable String activityID){
        activityService.deleteActivity(activityID);

        ApiSuccessResponse<String> response = new ApiSuccessResponse<>(
                "Session Deleted",
                activityID
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
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

    @GetMapping("/getAllActivity")
    public ResponseEntity<ApiSuccessResponse<List<Activity>>> getAllActivities(){

        List<Activity> data =  activityService.getAllActivities();

        ApiSuccessResponse<List<Activity>> response = new ApiSuccessResponse<>(
                "Activities fetched successfully" ,
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
