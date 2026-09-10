package com.husyairi.ManaProgressAku.DTO.Exercise.External;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
    FOR SINGLE EXERCISE FETCH
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchAscendExerciseByIdResponse {
    private boolean success;
    private AscendExerciseDTO data;

    public SearchAscendExerciseByIdResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public AscendExerciseDTO getData() {
        return data;
    }

    public void setData(AscendExerciseDTO data) {
        this.data = data;
    }
}
