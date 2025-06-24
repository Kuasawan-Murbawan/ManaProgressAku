package com.husyairi.ManaProgressAku.Service;

import com.husyairi.ManaProgressAku.DTO.InsertExerciseRequest;
import com.husyairi.ManaProgressAku.DTO.InsertExerciseResponse;

public interface ExerciseService {

    InsertExerciseResponse insertExercise(InsertExerciseRequest req);

}
