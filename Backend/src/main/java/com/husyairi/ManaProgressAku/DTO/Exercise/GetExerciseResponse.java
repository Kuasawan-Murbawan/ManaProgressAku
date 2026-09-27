package com.husyairi.ManaProgressAku.DTO.Exercise;

import com.husyairi.ManaProgressAku.Enums.Equipment;

public class GetExerciseResponse {

    private String exerciseName;
    private String exerciseType;
    private String info;
    private Boolean isBodyweight;
    private Equipment equipment;
    private String ascendExerciseId;

    public GetExerciseResponse(String exerciseName, String exerciseType, String info, Boolean isBodyweight, Equipment equipment, String ascendExerciseId) {
        this.exerciseName = exerciseName;
        this.info = info;
        this.isBodyweight = isBodyweight;
        this.exerciseType = exerciseType;
        this.equipment = equipment;
        this.ascendExerciseId = ascendExerciseId;
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

    public String getAscendExerciseId() {
        return ascendExerciseId;
    }

    public void setAscendExerciseId(String ascendExerciseId) {
        this.ascendExerciseId = ascendExerciseId;
    }
}
