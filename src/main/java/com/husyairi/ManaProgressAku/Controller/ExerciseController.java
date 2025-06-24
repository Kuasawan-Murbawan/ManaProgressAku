package com.husyairi.ManaProgressAku.Controller;

import com.husyairi.ManaProgressAku.DTO.GetExerciseResponse;
import com.husyairi.ManaProgressAku.DTO.InsertExerciseRequest;
import com.husyairi.ManaProgressAku.DTO.InsertExerciseResponse;
import com.husyairi.ManaProgressAku.ExceptionHandling.ApiErrorResponse;
import com.husyairi.ManaProgressAku.ExceptionHandling.ApiSuccessResponse;
import com.husyairi.ManaProgressAku.Service.ExerciseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

// define base path
@RequestMapping("/mana-progress-aku")
public class ExerciseController {

    private ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService){
        this.exerciseService = exerciseService;
    }

    @PostMapping("/insertExercise")
    public ResponseEntity<ApiSuccessResponse<InsertExerciseResponse>> insertExercise(@RequestBody InsertExerciseRequest request){
        InsertExerciseResponse data = exerciseService.insertExercise(request);

        // If error, it will be handled in Service
        ApiSuccessResponse<InsertExerciseResponse> response = new ApiSuccessResponse<>(
                "Exercise inserted successfully",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getExercise/{exerciseID}")
    public ResponseEntity<ApiSuccessResponse<GetExerciseResponse>> getExercise(@PathVariable Integer exerciseID) {
        GetExerciseResponse data = exerciseService.getExercise(exerciseID);

        // If error, it will be handled in Service
        ApiSuccessResponse<GetExerciseResponse> response = new ApiSuccessResponse<>(
                "Exercise fetched successfully",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
