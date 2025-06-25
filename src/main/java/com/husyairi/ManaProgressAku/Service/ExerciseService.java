package com.husyairi.ManaProgressAku.Service;

import com.husyairi.ManaProgressAku.DTO.GetExerciseResponse;
import com.husyairi.ManaProgressAku.DTO.InsertExerciseRequest;
import com.husyairi.ManaProgressAku.DTO.InsertExerciseResponse;
import com.husyairi.ManaProgressAku.DTO.UpdateExerciseRequest;

public interface ExerciseService {

    InsertExerciseResponse insertExercise(InsertExerciseRequest req);

    GetExerciseResponse getExercise(Integer exerciseID);

    InsertExerciseResponse updateExercise(UpdateExerciseRequest req);

    void deleteExercise(Integer exerciseID);
}
