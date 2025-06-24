package com.husyairi.ManaProgressAku.Service;

import com.husyairi.ManaProgressAku.DTO.GetExerciseResponse;
import com.husyairi.ManaProgressAku.DTO.InsertExerciseRequest;
import com.husyairi.ManaProgressAku.DTO.InsertExerciseResponse;

public interface ExerciseService {

    InsertExerciseResponse insertExercise(InsertExerciseRequest req);

    GetExerciseResponse getExercise(Integer exerciseID);
}
