package com.husyairi.ManaProgressAku.DTO;

public class GetExerciseResponse {
    private String exerciseName;

    private String info;

    public GetExerciseResponse() {
    }

    public GetExerciseResponse(String exerciseName, String info) {
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
