package com.husyairi.ManaProgressAku.DTO.Exercise.External;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AscendExerciseDTO {
    private String exerciseId;
    private String name;
    private String imageUrl;
    private String overview;
    private List<String> targetMuscles;
    private List<String> secondaryMuscles;
    private List<String> instructions;

    public AscendExerciseDTO() {
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getTargetMuscles() {
        return targetMuscles;
    }

    public void setTargetMuscles(List<String> targetMuscles) {
        this.targetMuscles = targetMuscles;
    }

    public List<String> getSecondaryMuscles() {
        return secondaryMuscles;
    }

    public void setSecondaryMuscles(List<String> secondaryMuscles) {
        this.secondaryMuscles = secondaryMuscles;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
