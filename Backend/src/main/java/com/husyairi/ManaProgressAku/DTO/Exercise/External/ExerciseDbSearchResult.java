package com.husyairi.ManaProgressAku.DTO.Exercise.External;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExerciseDbSearchResult {
    private String exerciseId;
    private String name;
    private String imageUrl;

    public ExerciseDbSearchResult() {
    }

    public ExerciseDbSearchResult(String exerciseId, String name, String imageUrl) {
        this.exerciseId = exerciseId;
        this.name = name;
        this.imageUrl = imageUrl;
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
}
