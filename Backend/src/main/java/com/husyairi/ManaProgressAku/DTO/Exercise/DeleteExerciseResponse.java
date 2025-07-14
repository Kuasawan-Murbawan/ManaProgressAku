package com.husyairi.ManaProgressAku.DTO.Exercise;

// DeleteExerciseResponse.java
public class DeleteExerciseResponse {
    private Integer exerciseID;

    public DeleteExerciseResponse(Integer exerciseID) {
        this.exerciseID = exerciseID;
    }

    public Integer getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(Integer exerciseID) {
        this.exerciseID = exerciseID;
    }
}
