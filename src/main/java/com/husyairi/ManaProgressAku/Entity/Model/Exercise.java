package com.husyairi.ManaProgressAku.Entity.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "exercise")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer exerciseID;

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private String exerciseName;

    private String generalInfo;

    public Exercise() {
    }

    public Exercise(String exerciseName, String generalInfo) {
        this.exerciseName = exerciseName;
        this.generalInfo = generalInfo;
    }

    public Exercise(Integer exerciseID, String exerciseName, String generalInfo) {
        this.exerciseID = exerciseID;
        this.exerciseName = exerciseName;
        this.generalInfo = generalInfo;
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
}
