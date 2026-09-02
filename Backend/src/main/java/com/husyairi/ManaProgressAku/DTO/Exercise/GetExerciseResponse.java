package com.husyairi.ManaProgressAku.DTO.Exercise;

public class GetExerciseResponse {
    private String exerciseName;

    private String info;

    private Boolean isBodyweight;

    // TODO: configure fetching exercise to accommodate isBodyweight

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
}
