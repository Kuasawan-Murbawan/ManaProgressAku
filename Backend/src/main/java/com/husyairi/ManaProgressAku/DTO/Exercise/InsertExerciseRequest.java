package com.husyairi.ManaProgressAku.DTO.Exercise;

import io.swagger.v3.oas.annotations.media.Schema;

public class InsertExerciseRequest {

    // TODO: configure inserting exercise to accommodate isBodyweight


    @Schema(example = "Preacher Curl", description = "Name of the exercise")
    private String exerciseName;

    @Schema(example = "Exercise that isolate the bicep area", description = "Short description of the exercise")
    private String generalInfo;

    @Schema(example = "1", description = "1 for Upper body, 2 for Lower Body")
    private String exerciseType;

    private Boolean isBodyweight;

    public InsertExerciseRequest(String exerciseName, String generalInfo, String exerciseType, Boolean isBodyweight) {
        this.exerciseName = exerciseName;
        this.generalInfo = generalInfo;
        this.exerciseType = exerciseType;
        this.isBodyweight = isBodyweight;
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

    public Boolean getBodyweight() {
        return isBodyweight;
    }

    public void setBodyweight(Boolean bodyweight) {
        isBodyweight = bodyweight;
    }
}

