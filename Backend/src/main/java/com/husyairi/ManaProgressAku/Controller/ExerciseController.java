package com.husyairi.ManaProgressAku.Controller;

import com.husyairi.ManaProgressAku.DTO.Exercise.*;
import com.husyairi.ManaProgressAku.Entity.Model.Exercise;
import com.husyairi.ManaProgressAku.ExceptionHandling.ApiSuccessResponse;
import com.husyairi.ManaProgressAku.Service.ExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Exercise", description = "CRUD for Exercise")
@CrossOrigin("*")
@RestController
public class ExerciseController {

    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService){
        this.exerciseService = exerciseService;
    }

    @Operation(
            summary = "Insert new exercise",
            description = "Create new exercise using Exercise name, info and type"
    )
    @PreAuthorize("hasRole('ADMIN')")
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

    @Operation(
            summary = "Get an exercise",
            description = "Return an exercise using Exercise ID"
    )
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

    @Operation(
            summary = "Update Exercise",
            description = "Update an exercise using all exercise details in request body"
    )
    @PreAuthorize("hasRole('ADMIN')")
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

    @Operation(
            summary = "Delete exercise",
            description = "Delete exercise using exercise ID"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteExercise/{exerciseID}")
    public ResponseEntity<ApiSuccessResponse<DeleteExerciseResponse>> deleteExercise(@PathVariable Integer exerciseID){

        exerciseService.deleteExercise(exerciseID);

        DeleteExerciseResponse response = new DeleteExerciseResponse(exerciseID);

        return ResponseEntity.ok(
                new ApiSuccessResponse<>("Exercise deleted successfully", response)
        );
    }

    @Operation(
            summary = "Get All Exercises",
            description = "returns all exercise - accessible for all user types"
    )
    @GetMapping("/getAllExercises")
    public ResponseEntity<ApiSuccessResponse<List<Exercise>>> getAllExercise(){

        List<Exercise> allExercises = exerciseService.getAllExercise();

        return ResponseEntity.ok(
                new ApiSuccessResponse<>("All exercises fetched successfully",
                        allExercises)
        );
    }
}
