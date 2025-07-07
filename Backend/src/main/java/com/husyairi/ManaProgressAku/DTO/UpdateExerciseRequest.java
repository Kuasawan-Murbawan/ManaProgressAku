package com.husyairi.ManaProgressAku.DTO;

public class UpdateExerciseRequest {
    private Integer exerciseID;
    private String exerciseName;
    private String exerciseType;
    private String generalInfo;

    public UpdateExerciseRequest() {
    }

    public UpdateExerciseRequest(Integer exerciseID, String exerciseName, String generalInfo, String exerciseType) {
        this.exerciseID = exerciseID;
        this.exerciseName = exerciseName;
        this.generalInfo = generalInfo;
        this.exerciseType = exerciseType;
    }

    public Integer getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(Integer exerciseID) {
        this.exerciseID = exerciseID;
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
