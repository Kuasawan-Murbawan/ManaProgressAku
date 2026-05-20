package com.husyairi.ManaProgressAku.DTO.Exercise;

import io.swagger.v3.oas.annotations.media.Schema;

public class InsertExerciseRequest {

    @Schema(example = "Preacher Curl", description = "Name of the exercise")
    private String exerciseName;

    @Schema(example = "Exercise that isolate the bicep area", description = "Short description of the exercise")
    private String generalInfo;

    @Schema(example = "1", description = "1 for Upper body, 2 for Lower Body")
    private String exerciseType;

    public InsertExerciseRequest(String exerciseName, String generalInfo, String exerciseType) {
        this.exerciseName = exerciseName;
        this.generalInfo = generalInfo;
        this.exerciseType = exerciseType;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getGeneralInfo() {
        return generalInfo;
    }

    public void setGeneralInfo(String generalInfo) {
        this.generalInfo = generalInfo;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }
}
