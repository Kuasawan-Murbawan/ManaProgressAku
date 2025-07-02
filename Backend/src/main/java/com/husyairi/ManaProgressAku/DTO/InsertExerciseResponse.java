package com.husyairi.ManaProgressAku.DTO;

public class InsertExerciseResponse {
    private String exerciseName;
    private String info;

    public InsertExerciseResponse() {
    }

    public InsertExerciseResponse(String exerciseName, String info) {
        this.exerciseName = exerciseName;
        this.info = info;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
