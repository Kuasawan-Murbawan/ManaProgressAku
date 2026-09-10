package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.Exercise.External.AscendExerciseDTO;
import com.husyairi.ManaProgressAku.DTO.Exercise.External.ExerciseDbSearchResult;
import com.husyairi.ManaProgressAku.DTO.Exercise.External.SearchAscendExerciseByIdResponse;
import com.husyairi.ManaProgressAku.DTO.Exercise.External.SearchMultipleAscendExerciseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
public class AscendApiClient {

    private static final Logger log = LoggerFactory.getLogger(AscendApiClient.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AscendApiClient(
            RestTemplate ascendApiRestTemplate,
            @Value("${rapidapi.exercisedb.base-url}") String baseUrl
    ) {
        this.restTemplate = ascendApiRestTemplate;
        this.baseUrl = baseUrl;
    }

    public boolean isLive(){
        try{
            log.info("Checking Ascend API status");
            restTemplate.getForEntity(baseUrl + "/liveness", String.class);
            return  true;
        }catch (Exception e){
            log.warn("Ascend API liveness check failed: {}", e.getMessage());
            return false;
        }
    }

    public Optional<AscendExerciseDTO> getExerciseById(String ascendExerciseId){
        String url = baseUrl + "/exercises/" + ascendExerciseId;

        try{
            log.info("Fetching exercise from AscendAPI: {}", ascendExerciseId);

            SearchAscendExerciseByIdResponse response = restTemplate.getForObject(url, SearchAscendExerciseByIdResponse.class);
            if (response == null || response.getData() == null) {
                return Optional.empty();
            }
            return Optional.of(response.getData());

        } catch (HttpClientErrorException.NotFound e) {
            log.info("AscendAPI has no exercise for id: {}", ascendExerciseId);
            return Optional.empty();

        } catch (RestClientException e) {
            log.error("AscendAPI call failed for exercise id {}: {}", ascendExerciseId, e.getMessage());
            throw e;
        }
    }

    public List<ExerciseDbSearchResult> searchExercises(String search){
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/exercises/search")
                .queryParam("search", search)
                .build()
                .toUriString();

        try {
            log.info("Searching AscendAPI for: {}", search);
            SearchMultipleAscendExerciseResponse response = restTemplate.getForObject(url, SearchMultipleAscendExerciseResponse.class);

            if (response == null || response.getData() == null) {
                return List.of();
            }

            return response.getData();

        } catch (RestClientException e) {
            log.error("AscendAPI search failed for query '{}': {}", search, e.getMessage());
            throw e;
        }
    }

}
