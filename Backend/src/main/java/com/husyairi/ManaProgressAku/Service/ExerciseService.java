package com.husyairi.ManaProgressAku.Service;

import com.husyairi.ManaProgressAku.DTO.Exercise.*;
import com.husyairi.ManaProgressAku.Entity.Model.Exercise;

import java.util.List;

public interface ExerciseService {

    InsertExerciseResponse insertExercise(InsertExerciseRequest req);

    GetExerciseResponse getExercise(Integer exerciseID);

    InsertExerciseResponse updateExercise(UpdateExerciseRequest req);

    InsertExerciseResponse linkExerciseToDb(Integer exerciseID, LinkExerciseDbRequest request);

    void deleteExercise(Integer exerciseID);

    List<Exercise> getAllExercise();
}
