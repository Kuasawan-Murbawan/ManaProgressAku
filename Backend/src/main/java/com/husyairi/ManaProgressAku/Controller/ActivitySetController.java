package com.husyairi.ManaProgressAku.Controller;

import com.husyairi.ManaProgressAku.DTO.ActivitySet.GetSetResponse;
import com.husyairi.ManaProgressAku.DTO.ActivitySet.InsertSetRequest;
import com.husyairi.ManaProgressAku.Entity.Model.ActivitySet;
import com.husyairi.ManaProgressAku.ExceptionHandling.ApiSuccessResponse;
import com.husyairi.ManaProgressAku.Service.ActivityService;
import com.husyairi.ManaProgressAku.Service.ActivitySetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class ActivitySetController {

    private final ActivitySetService activitySetService;

    private static final Logger logger = LoggerFactory.getLogger(ActivitySetController.class);

    @Autowired
    public ActivitySetController(ActivitySetService activitySetService){
        this.activitySetService = activitySetService;
    }

    @PostMapping("/insertSet")
    public ResponseEntity<ApiSuccessResponse<GetSetResponse>> insertActivitySet (@RequestBody InsertSetRequest request){
        GetSetResponse data = activitySetService.insertSet(request);

        ApiSuccessResponse<GetSetResponse> response = new ApiSuccessResponse<>(
                "Set inserted successfully",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/deleteSet/{setID}")
    public ResponseEntity<ApiSuccessResponse<Long>> deleteActivitySet(@PathVariable Long setID){

        Long data = activitySetService.deleteSet(setID);

        ApiSuccessResponse<Long> response = new ApiSuccessResponse<>(
                "Set deleted successfully",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getSet/{setID}")
    public ResponseEntity<ApiSuccessResponse<GetSetResponse>> getSet (@PathVariable Long setID){

        GetSetResponse data = activitySetService.getSet(setID);

        ApiSuccessResponse<GetSetResponse> response = new ApiSuccessResponse<>(
                "Set fetched successfully",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/editSet/{setID}")
    public ResponseEntity<ApiSuccessResponse<GetSetResponse>> editSet(@PathVariable Long setID, @RequestBody InsertSetRequest request){
        GetSetResponse data = activitySetService.editSet(request, setID);

        ApiSuccessResponse<GetSetResponse> response = new ApiSuccessResponse<>(
                "Set updated sucessfully",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getSetByActivity/{activityID}")
    public ResponseEntity<ApiSuccessResponse<List<ActivitySet>>> getSetByActivity (@PathVariable String activityID){

        List<ActivitySet> data = activitySetService.getAllSetByActivity(activityID);

        ApiSuccessResponse<List<ActivitySet>> response = new ApiSuccessResponse<>(
                "All sets fetched successfully",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
