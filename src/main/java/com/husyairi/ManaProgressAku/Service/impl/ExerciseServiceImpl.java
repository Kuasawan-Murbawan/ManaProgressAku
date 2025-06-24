package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.GetExerciseResponse;
import com.husyairi.ManaProgressAku.DTO.InsertExerciseRequest;
import com.husyairi.ManaProgressAku.DTO.InsertExerciseResponse;
import com.husyairi.ManaProgressAku.Entity.Model.Exercise;
import com.husyairi.ManaProgressAku.Repository.ExerciseRepository;
import com.husyairi.ManaProgressAku.Service.ExerciseService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    private static final Logger log = LoggerFactory.getLogger(ExerciseServiceImpl.class);

    private final ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseServiceImpl(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public InsertExerciseResponse insertExercise(InsertExerciseRequest req){

        Exercise newExercise = new Exercise(req.getExerciseName(), req.getGeneralInfo());

        try {
            Exercise savedExercise = exerciseRepository.save(newExercise);
            return new InsertExerciseResponse(
                    savedExercise.getExerciseName(),
                    savedExercise.getGeneralInfo()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GetExerciseResponse getExercise(Integer exerciseID){

        try {
            Optional<Exercise> fetchExercise = exerciseRepository.findById(exerciseID);

            if(fetchExercise.isPresent()){
                Exercise exercise = fetchExercise.get();
                return new GetExerciseResponse(exercise.getExerciseName(), exercise.getGeneralInfo());
            }else{
                throw new EntityNotFoundException("Exercise with ID " + exerciseID + " not found.");
            }
        }catch (EntityNotFoundException e) {
            throw e; // Rethrow the specific exception if exercise is not found
        } catch (Exception e) {
            // Log the exception if needed and rethrow a more specific or general error
            throw new RuntimeException("Error fetching exercise: " + e.getMessage(), e);
        }

    }

}
