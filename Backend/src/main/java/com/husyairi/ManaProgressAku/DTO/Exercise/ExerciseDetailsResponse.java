package com.husyairi.ManaProgressAku.DTO.Exercise;
import java.util.List;

/*
    this dto act as a buffer between the ascendapi and frontend
    Why we do it?
        - if AscendAPI change naming convention, it will break the frontend because the name will be different
        - so by having this, we can map the data from ascendapi BE to our BE
 */
public class ExerciseDetailsResponse {

    private boolean available;
    private String imageUrl;
    private String overview;
    private List<String> targetMuscles;
    private List<String> secondaryMuscles;
    private List<String> instructions;

    public ExerciseDetailsResponse() {
    }

    public static ExerciseDetailsResponse of(
            String imageUrl,
            String overview,
            List<String> targetMuscles,
            List<String> secondaryMuscles,
            List<String> instructions
    ) {
        ExerciseDetailsResponse response = new ExerciseDetailsResponse();
        response.available = true;
        response.imageUrl = imageUrl;
        response.overview = overview;
        response.targetMuscles = targetMuscles;
        response.secondaryMuscles = secondaryMuscles;
        response.instructions = instructions;
        return response;
    }

    // If there are no match, return this DTO but all null values
    public static ExerciseDetailsResponse unavailable(){
        return new ExerciseDetailsResponse();
    }

    // We dont put setter bc we only limit to have the name during creation
    // we dont change the values bc the values is from AScendapi so we dont have the power
    // to change it
    public boolean isAvailable() {
        return available;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getOverview() {
        return overview;
    }

    public List<String> getTargetMuscles() {
        return targetMuscles;
    }

    public List<String> getSecondaryMuscles() {
        return secondaryMuscles;
    }

    public List<String> getInstructions() {
        return instructions;
    }

}
