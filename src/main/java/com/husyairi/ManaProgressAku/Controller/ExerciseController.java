package com.husyairi.ManaProgressAku.Controller;

import com.husyairi.ManaProgressAku.DTO.InsertExerciseRequest;
import com.husyairi.ManaProgressAku.DTO.InsertExerciseResponse;
import com.husyairi.ManaProgressAku.Service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public InsertExerciseResponse insertExercise(@RequestBody InsertExerciseRequest request){
        return exerciseService.insertExercise(request);
    }
}
