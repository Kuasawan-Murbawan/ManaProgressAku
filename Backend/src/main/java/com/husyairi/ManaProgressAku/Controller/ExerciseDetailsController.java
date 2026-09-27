package com.husyairi.ManaProgressAku.Controller;


import com.husyairi.ManaProgressAku.DTO.Exercise.ExerciseDetailsResponse;
import com.husyairi.ManaProgressAku.DTO.Exercise.External.ExerciseDbSearchResult;
import com.husyairi.ManaProgressAku.ExceptionHandling.ApiSuccessResponse;
import com.husyairi.ManaProgressAku.Service.ExerciseDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ExerciseDetails", description = "External exercise enrichment data via ExerciseDB")
@CrossOrigin("*")
@RestController
public class ExerciseDetailsController {

    private final ExerciseDetailsService exerciseDetailsService;

    @Autowired
    public ExerciseDetailsController(ExerciseDetailsService exerciseDetailsService){
        this.exerciseDetailsService = exerciseDetailsService;
    }

    @Operation(
            summary = "Get exercise enrichment details",
            description = "Returns image, overview, and targeted muscles for an exercise, if linked to ExerciseDB. Returns available=false if not linked or if the lookup fails."
    )
    @GetMapping
    public ResponseEntity<ApiSuccessResponse<ExerciseDetailsResponse>> getExerciseDetails(@PathVariable Integer exerciseID){
        ExerciseDetailsResponse data = exerciseDetailsService.getDetailsForExercise(exerciseID);

        ApiSuccessResponse<ExerciseDetailsResponse> response = new ApiSuccessResponse<>(
                "Exercise details fetched",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Search ExerciseDB for admin matching",
            description = "Admin-only. Searches ExerciseDB by name so an admin can link a local exercise to an ExerciseDB entry."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/searchExerciseDbMatches")
    public ResponseEntity<ApiSuccessResponse<List<ExerciseDbSearchResult>>> searchExerciseDbMatches(
            @RequestParam String query
    ) {
        List<ExerciseDbSearchResult> data = exerciseDetailsService.searchForAdminMatching(query);

        ApiSuccessResponse<List<ExerciseDbSearchResult>> response = new ApiSuccessResponse<>(
                "Search complete",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
