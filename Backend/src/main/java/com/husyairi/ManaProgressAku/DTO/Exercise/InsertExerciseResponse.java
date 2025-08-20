package com.husyairi.ManaProgressAku.DTO.Exercise;

public class InsertExerciseResponse {
    private String exerciseName;
    private String exerciseType;
    private String info;

    public InsertExerciseResponse() {
    }

    public InsertExerciseResponse(String exerciseName, String exerciseType, String info) {
        this.exerciseName = exerciseName;
        this.exerciseType = exerciseType;
        this.info = info;
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
}
