package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.Exercise.*;
import com.husyairi.ManaProgressAku.Entity.Model.Exercise;
import com.husyairi.ManaProgressAku.ExceptionHandling.BadRequestException;
import com.husyairi.ManaProgressAku.Repository.ActivityRepository;
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
    private final ActivityRepository activityRepository;


    @Autowired
    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, ActivityRepository activityRepository) {
        this.exerciseRepository = exerciseRepository;
        this.activityRepository = activityRepository;
    }

    @Override
    public InsertExerciseResponse insertExercise(InsertExerciseRequest req){

        Exercise newExercise = new Exercise(req.getExerciseName(), req.getExerciseType(), req.getGeneralInfo(), req.getBodyweight(), req.getEquipment());

        if(req.getExerciseName() == null || req.getExerciseType() == null){
            throw new BadRequestException(400, "Please fill in all details", new HashMap<>());
        }

        try {
            Exercise savedExercise = exerciseRepository.save(newExercise);
            return new InsertExerciseResponse(
                    savedExercise.getExerciseID(),
                    savedExercise.getExerciseName(),
                    savedExercise.getExerciseType(),
                    savedExercise.getGeneralInfo(),
                    savedExercise.getIsBodyweight(),
                    savedExercise.getEquipment(),
                    savedExercise.getAscendExerciseId()
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
            return new GetExerciseResponse(
                    exercise.getExerciseName(),
                    exercise.getExerciseType(),
                    exercise.getGeneralInfo(),
                    exercise.getIsBodyweight(),
                    exercise.getEquipment(),
                    exercise.getAscendExerciseId());
        }else{
            throw new BadRequestException(404, "Exercise ID not found", new HashMap<>());
        }
    }

    @Override
    public InsertExerciseResponse updateExercise(UpdateExerciseRequest req){

        // Check if ID exist
        Optional<Exercise> isExist = exerciseRepository.findById(req.getExerciseID());
        if(isExist.isEmpty()){
            throw new BadRequestException(404, "Exercise with ID " + req.getExerciseID() + " not found.", new HashMap<>());
        }

        Exercise updatedExercise = isExist.get();
        updatedExercise.setExerciseName(req.getExerciseName());
        updatedExercise.setGeneralInfo(req.getGeneralInfo());
        updatedExercise.setExerciseType(req.getExerciseType());
        updatedExercise.setIsBodyweight(req.getIsBodyweight());
        updatedExercise.setEquipment(req.getEquipment());

        try {
            // Save inside database
            exerciseRepository.save(updatedExercise);
        }catch (Exception e){
            throw new BadRequestException(500, "", new HashMap<>());
        }

        return new InsertExerciseResponse(
                updatedExercise.getExerciseID(),
                updatedExercise.getExerciseName(),
                updatedExercise.getExerciseType(),
                updatedExercise.getGeneralInfo(),
                updatedExercise.getIsBodyweight(),
                updatedExercise.getEquipment(),
                updatedExercise.getAscendExerciseId()
        );
    }

    @Override
    public InsertExerciseResponse linkExerciseToDb(Integer exerciseID, LinkExerciseDbRequest req){
        Optional<Exercise> isExist = exerciseRepository.findById(exerciseID);
        if(isExist.isEmpty()){
            throw new BadRequestException(404, "Exercise with ID " + exerciseID + " not found", new HashMap<>());
        }
        Exercise exercise = isExist.get();
        exercise.setAscendExerciseId(req.getAscendExerciseId());

        try {
            exerciseRepository.save(exercise);
        } catch (Exception e) {
            throw new BadRequestException(500, "Failed to link exercise", new HashMap<>());
        }

        return new InsertExerciseResponse(
                exercise.getExerciseID(),
                exercise.getExerciseName(),
                exercise.getExerciseType(),
                exercise.getGeneralInfo(),
                exercise.getIsBodyweight(),
                exercise.getEquipment(),
                exercise.getAscendExerciseId()
        );
    }

    @Override
    public void deleteExercise (Integer exerciseID){
        Optional<Exercise> isExist = exerciseRepository.findById(exerciseID);

        // Activity.exerciseID is a plain column, not a foreign key, so the database
        // would happily delete an exercise that logged workouts still point at.
        if(activityRepository.existsByExerciseID(exerciseID)){
            throw new BadRequestException(409,
                    "\"" + isExist.get().getExerciseName() + "\" has been used in logged workouts, so it can't be deleted.",
                    new HashMap<>());
        }

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
