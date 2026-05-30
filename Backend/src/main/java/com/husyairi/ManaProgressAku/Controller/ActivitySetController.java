package com.husyairi.ManaProgressAku.Controller;

import com.husyairi.ManaProgressAku.DTO.ActivitySet.GetSetResponse;
import com.husyairi.ManaProgressAku.DTO.ActivitySet.InsertSetRequest;
import com.husyairi.ManaProgressAku.ExceptionHandling.ApiSuccessResponse;
import com.husyairi.ManaProgressAku.Service.ActivityService;
import com.husyairi.ManaProgressAku.Service.ActivitySetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class ActivitySetController {

    private final ActivitySetService activitySetService;

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

    @DeleteMapping("/deleteSet")
    public ResponseEntity<ApiSuccessResponse<Long>> deleteActivitySet(@PathVariable Long setID){

        Long data = activitySetService.deleteSet(setID);

        ApiSuccessResponse<Long> response = new ApiSuccessResponse<>(
                "Set deleted successfully",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
