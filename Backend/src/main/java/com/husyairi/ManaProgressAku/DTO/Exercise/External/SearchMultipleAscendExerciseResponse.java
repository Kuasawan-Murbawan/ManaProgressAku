package com.husyairi.ManaProgressAku.DTO.Exercise.External;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
/*
    FOR MULTIPLE EXERCISES FETCH
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchMultipleAscendExerciseResponse {
    private boolean success;
    private List<ExerciseDbSearchResult> data;

    public SearchMultipleAscendExerciseResponse() {
    }
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ExerciseDbSearchResult> getData() {
        return data;
    }

    public void setData(List<ExerciseDbSearchResult> data) {
        this.data = data;
    }
}
