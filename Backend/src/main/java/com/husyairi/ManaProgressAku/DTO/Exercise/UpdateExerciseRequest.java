package com.husyairi.ManaProgressAku.DTO.Exercise;

import com.husyairi.ManaProgressAku.Enums.Equipment;

public class UpdateExerciseRequest {
    private Integer exerciseID;
    private String exerciseName;
    private String exerciseType;
    private String generalInfo;
    private Boolean isBodyweight;
    private Equipment equipment;

    public UpdateExerciseRequest() {
    }

    public UpdateExerciseRequest(Integer exerciseID, String exerciseName, String generalInfo, String exerciseType, Boolean isBodyweight, Equipment equipment) {
        this.exerciseID = exerciseID;
        this.exerciseName = exerciseName;
        this.generalInfo = generalInfo;
        this.exerciseType = exerciseType;
        this.isBodyweight = isBodyweight;
        this.equipment = equipment;
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

    public Boolean getIsBodyweight() {
        return isBodyweight;
    }

    public void setIsBodyweight(Boolean isBodyweight) {
        this.isBodyweight = isBodyweight;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }
}
