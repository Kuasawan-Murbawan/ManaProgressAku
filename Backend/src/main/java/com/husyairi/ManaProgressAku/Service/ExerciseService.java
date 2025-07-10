package com.husyairi.ManaProgressAku.Service;

import com.husyairi.ManaProgressAku.DTO.Exercise.GetExerciseResponse;
import com.husyairi.ManaProgressAku.DTO.Exercise.InsertExerciseRequest;
import com.husyairi.ManaProgressAku.DTO.Exercise.InsertExerciseResponse;
import com.husyairi.ManaProgressAku.DTO.Exercise.UpdateExerciseRequest;
import com.husyairi.ManaProgressAku.Entity.Model.Exercise;

import java.util.List;

public interface ExerciseService {

    InsertExerciseResponse insertExercise(InsertExerciseRequest req);

    GetExerciseResponse getExercise(Integer exerciseID);

    InsertExerciseResponse updateExercise(UpdateExerciseRequest req);

    void deleteExercise(Integer exerciseID);

    List<Exercise> getAllExercise();
}
