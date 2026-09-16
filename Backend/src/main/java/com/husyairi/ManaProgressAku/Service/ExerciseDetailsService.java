package com.husyairi.ManaProgressAku.Service;

import com.husyairi.ManaProgressAku.DTO.Exercise.ExerciseDetailsResponse;
import com.husyairi.ManaProgressAku.DTO.Exercise.External.SearchMultipleAscendExerciseResponse;

import java.util.List;

public interface ExerciseDetailsService {

    ExerciseDetailsResponse getDetailsForExercise(Integer exerciseID);

    List<SearchMultipleAscendExerciseResponse> searchForAdminMatching(String query);
}
