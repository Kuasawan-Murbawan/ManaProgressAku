package com.husyairi.ManaProgressAku.DTO.Exercise;

public class InsertExerciseResponse {
    private Integer exerciseID;
    private String exerciseName;
    private String exerciseType;
    private String info;
    private Boolean isBodyweight;
    private String equipment;
    private String ascendExerciseId;

    public InsertExerciseResponse(Integer exerciseID, String exerciseName, String exerciseType, String info, Boolean isBodyweight, String equipment, String ascendExerciseId) {
        this.exerciseID = exerciseID;
        this.exerciseName = exerciseName;
        this.exerciseType = exerciseType;
        this.info = info;
        this.isBodyweight = isBodyweight;
        this.equipment = equipment;
        this.ascendExerciseId = ascendExerciseId;
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

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getAscendExerciseId() {
        return ascendExerciseId;
    }

    public void setAscendExerciseId(String ascendExerciseId) {
        this.ascendExerciseId = ascendExerciseId;
    }
}
