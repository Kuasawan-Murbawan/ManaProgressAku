package com.husyairi.ManaProgressAku.DTO.Exercise;

public class InsertExerciseResponse {
    private String exerciseName;
    private String exerciseType;
    private String info;
    private Boolean isBodyweight;

    // TODO: configure response of inserting exercise to accommodate isBodyweight


    public InsertExerciseResponse(String exerciseName, String exerciseType, String info, Boolean isBodyweight) {
        this.exerciseName = exerciseName;
        this.exerciseType = exerciseType;
        this.info = info;
        this.isBodyweight = isBodyweight;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Boolean getBodyweight() {
        return isBodyweight;
    }

    public void setBodyweight(Boolean bodyweight) {
        isBodyweight = bodyweight;
    }
}
