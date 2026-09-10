package com.husyairi.ManaProgressAku.DTO.Exercise;

public class GetExerciseResponse {

    private String exerciseName;
    private String info;
    private Boolean isBodyweight;
    private String equipment;
    private String ascendExerciseId;

    public GetExerciseResponse(String exerciseName, String info, Boolean isBodyweight) {
        this.exerciseName = exerciseName;
        this.info = info;
        this.isBodyweight = isBodyweight;
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

    public Boolean getIsBodyweight() {
        return isBodyweight;
    }

    public void setIsBodyweight(Boolean isBodyweight) {
        this.isBodyweight = isBodyweight;
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
