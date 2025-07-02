package com.husyairi.ManaProgressAku.DTO;

public class InsertExerciseRequest {
    private String exerciseName;
    private String generalInfo;

    public InsertExerciseRequest(String exerciseName, String generalInfo) {
        this.exerciseName = exerciseName;
        this.generalInfo = generalInfo;
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
}
