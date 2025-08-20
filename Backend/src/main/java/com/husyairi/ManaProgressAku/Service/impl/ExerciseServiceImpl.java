package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.Exercise.GetExerciseResponse;
import com.husyairi.ManaProgressAku.DTO.Exercise.InsertExerciseRequest;
import com.husyairi.ManaProgressAku.DTO.Exercise.InsertExerciseResponse;
import com.husyairi.ManaProgressAku.DTO.Exercise.UpdateExerciseRequest;
import com.husyairi.ManaProgressAku.Entity.Model.Exercise;
import com.husyairi.ManaProgressAku.ExceptionHandling.BadRequestException;
import com.husyairi.ManaProgressAku.Repository.ExerciseRepository;
import com.husyairi.ManaProgressAku.Service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseServiceImpl(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public InsertExerciseResponse insertExercise(InsertExerciseRequest req){

        Exercise newExercise = new Exercise(req.getExerciseName(), req.getGeneralInfo());

        if(req.getExerciseName() == null){
            throw new BadRequestException(400, "Please fill in all details", new HashMap<>());
        }

        try {
            Exercise savedExercise = exerciseRepository.save(newExercise);
            return new InsertExerciseResponse(
                    savedExercise.getExerciseName(),
                    savedExercise.getExerciseType(),
                    savedExercise.getGeneralInfo()
            );
        } catch (Exception e) {
            throw new BadRequestException(400, e.getMessage(), new HashMap<>());
        }
    }

    @Override
    public GetExerciseResponse getExercise(Integer exerciseID){
        Optional<Exercise> fetchExercise = exerciseRepository.findById(exerciseID);

        if(fetchExercise.isPresent()){
            Exercise exercise = fetchExercise.get();
            return new GetExerciseResponse(exercise.getExerciseName(), exercise.getGeneralInfo());
        }else{
            throw new BadRequestException(404, "Exercise ID not found", new HashMap<>());
        }
    }

    @Override
    // TODO: decide whether to create UpdateResponse
    // or can return the updated Session itself
    public InsertExerciseResponse updateExercise(UpdateExerciseRequest req){

        // Check if ID exist
        Optional<Exercise> isExist = exerciseRepository.findById(req.getExerciseID());
        if(isExist.isEmpty()){
            throw new BadRequestException(404, "Exercise with ID " + req.getExerciseID() + " not found.", new HashMap<>());
        }

        Exercise updatedExercise = isExist.get();
        updatedExercise.setExerciseName(req.getExerciseName());
        updatedExercise.setGeneralInfo(req.getGeneralInfo());
        try {
            // Save inside database
            exerciseRepository.save(updatedExercise);
        }catch (Exception e){
            throw new BadRequestException(500, "", new HashMap<>());
        }

        return new InsertExerciseResponse(
                updatedExercise.getExerciseName(),
                updatedExercise.getExerciseType(),
                updatedExercise.getGeneralInfo()
        );
    }

    @Override
    public void deleteExercise (Integer exerciseID){
        Optional<Exercise> isExist = exerciseRepository.findById(exerciseID);

        if(isExist.isEmpty()){
            throw new BadRequestException(404, "Exercise ID " + exerciseID + " not found.", new HashMap<>());
        }

        try{
            exerciseRepository.deleteById(exerciseID);
        }catch (Exception e){
            throw new BadRequestException(500,
                    "An error occurred while deleting Exercise ID " + exerciseID,
                    Map.of("error", e.getMessage()));
        }
    }

    @Override
    public List<Exercise> getAllExercise(){

        return exerciseRepository.findAll();
    }
}
