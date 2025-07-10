package com.husyairi.ManaProgressAku.DTO.Exercise;

public class InsertExerciseRequest {
    private String exerciseName;
    private String generalInfo;
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
