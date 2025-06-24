package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.InsertExerciseRequest;
import com.husyairi.ManaProgressAku.DTO.InsertExerciseResponse;
import com.husyairi.ManaProgressAku.Entity.Model.Exercise;
import com.husyairi.ManaProgressAku.Repository.ExerciseRepository;
import com.husyairi.ManaProgressAku.Service.ExerciseService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        Exercise savedExercise = exerciseRepository.save(newExercise);

//        log.info("in newExercise: " + newExercise.getExerciseName() + newExercise.getGeneralInfo());

        return new InsertExerciseResponse(
                savedExercise.getExerciseName(),
                savedExercise.getGeneralInfo()
        );
    }
}
