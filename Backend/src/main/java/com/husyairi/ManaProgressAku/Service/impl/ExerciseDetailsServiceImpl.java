package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.Exercise.ExerciseDetailsResponse;
import com.husyairi.ManaProgressAku.DTO.Exercise.External.AscendExerciseDTO;
import com.husyairi.ManaProgressAku.DTO.Exercise.External.SearchMultipleAscendExerciseResponse;
import com.husyairi.ManaProgressAku.Entity.Model.Exercise;
import com.husyairi.ManaProgressAku.ExceptionHandling.RateLimitExceededException;
import com.husyairi.ManaProgressAku.Repository.ExerciseRepository;
import com.husyairi.ManaProgressAku.Service.ExerciseDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class ExerciseDetailsServiceImpl implements ExerciseDetailsService {

    private static final Logger log = LoggerFactory.getLogger(ExerciseDetailsServiceImpl.class);
    private final ExerciseRepository exerciseRepository;
    private final ExerciseDetailsCache cache;
    private final AscendApiRateLimiter rateLimiter;
    private final AscendApiClient ascendApiClient;

    public ExerciseDetailsServiceImpl(
            ExerciseRepository exerciseRepository,
            ExerciseDetailsCache cache,
            AscendApiRateLimiter rateLimiter,
            AscendApiClient ascendApiClient
    ) {
        this.exerciseRepository = exerciseRepository;
        this.cache = cache;
        this.rateLimiter = rateLimiter;
        this.ascendApiClient = ascendApiClient;
    }

    @Override
    public ExerciseDetailsResponse getDetailsForExercise(Integer exerciseID) {

        // Fetch the exercise needed from db
        Optional<Exercise> exerciseOpt = exerciseRepository.findById(exerciseID);

        // If there no result, then the exercise or exercise id not exist
        if (exerciseOpt.isEmpty()) {
            log.warn("getDetailsForExercise called with unknown exerciseID: {}", exerciseID);
            return ExerciseDetailsResponse.unavailable();
        }

        // extract the already matchinge ascendExerciseID
        String ascendExerciseId = exerciseOpt.get().getAscendExerciseId();

        // If we did not match them yet, we return unavailable
        if(ascendExerciseId == null || ascendExerciseId.isEmpty()){
            return ExerciseDetailsResponse.unavailable();
        }

        // If we have already the ascend api id, we fetch the other details
        try{
            Optional<AscendExerciseDTO> result = cache.getOrFetch(ascendExerciseId, () -> {
                // If the api call still under limit, try to fetch the api
                if(!rateLimiter.tryConsumeCall()){
                    throw new RateLimitExceededException("AscendAPI rate limit reached — refusing call for " + ascendExerciseId);
                }

                return ascendApiClient.getExerciseById(ascendExerciseId);
            });

            return result.map(dto -> ExerciseDetailsResponse.of(
                            dto.getImageUrl(),
                            dto.getOverview(),
                            dto.getTargetMuscles(),
                            dto.getSecondaryMuscles(),
                            dto.getInstructions()
                    ))
                    .orElse(ExerciseDetailsResponse.unavailable());

        } catch (Exception e) {
            log.error("Failed to fetch exercise details for ascendExerciseId {}: {}",
                    ascendExerciseId, e.getMessage());
            return ExerciseDetailsResponse.unavailable();
        }

    }

    @Override
    public List<SearchMultipleAscendExerciseResponse> searchForAdminMatching(String query) {
        return List.of();
    }
}
