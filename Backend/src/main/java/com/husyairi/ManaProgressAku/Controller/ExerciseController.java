package com.husyairi.ManaProgressAku.Controller;

import com.husyairi.ManaProgressAku.DTO.GetExerciseResponse;
import com.husyairi.ManaProgressAku.DTO.InsertExerciseRequest;
import com.husyairi.ManaProgressAku.DTO.InsertExerciseResponse;
import com.husyairi.ManaProgressAku.DTO.UpdateExerciseRequest;
import com.husyairi.ManaProgressAku.Entity.Model.Exercise;
import com.husyairi.ManaProgressAku.ExceptionHandling.ApiErrorResponse;
import com.husyairi.ManaProgressAku.ExceptionHandling.ApiSuccessResponse;
import com.husyairi.ManaProgressAku.Service.ExerciseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
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

    @PutMapping("/updateExercise")
    public ResponseEntity<ApiSuccessResponse<InsertExerciseResponse>> updateExercise(@RequestBody UpdateExerciseRequest request){

        // We use InsertExerciseResponse, and not Exercise as to not exposed sensitive data (ID)
        InsertExerciseResponse data = exerciseService.updateExercise(request);
        // Reuse insertResponse as to not make new response dto specifically for update
        ApiSuccessResponse<InsertExerciseResponse> response = new ApiSuccessResponse<>(
                "Exercise updated successfully",
                data
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/deleteExercise/{exerciseID}")
    public ResponseEntity<ApiSuccessResponse<String>> deleteExercise(@PathVariable Integer exerciseID){

        exerciseService.deleteExercise(exerciseID);

        return ResponseEntity.ok(
                new ApiSuccessResponse<>("Exercise deleted successfully", "Deleted ID: " + exerciseID)
        );
    }

    @GetMapping("/admin/getAllExercises")
    public ResponseEntity<ApiSuccessResponse<List<Exercise>>> getAllExercise(){

        List<Exercise> allExercises = exerciseService.getAllExercise();

        return ResponseEntity.ok(
                new ApiSuccessResponse<>("All exercises fetched successfully",
                        allExercises)
        );
    }
}
