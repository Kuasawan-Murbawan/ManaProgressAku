package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.Exercise.ExerciseDetailsResponse;
import com.husyairi.ManaProgressAku.DTO.Exercise.External.AscendExerciseDTO;
import com.husyairi.ManaProgressAku.DTO.Exercise.External.ExerciseDbSearchResult;
import com.husyairi.ManaProgressAku.Entity.Model.Exercise;
import com.husyairi.ManaProgressAku.ExceptionHandling.BadRequestException;
import com.husyairi.ManaProgressAku.Repository.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.husyairi.ManaProgressAku.support.BadRequestAssertions.thrownBadRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExerciseDetailsServiceImplTest {

    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private AscendApiRateLimiter rateLimiter;
    @Mock
    private AscendApiClient ascendApiClient;

    private ExerciseDetailsServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ExerciseDetailsServiceImpl(
                exerciseRepository, new ExerciseDetailsCache(), rateLimiter, ascendApiClient);
    }

    private void exerciseLinkedTo(String ascendId) {
        Exercise exercise = new Exercise();
        exercise.setExerciseID(1);
        exercise.setAscendExerciseId(ascendId);
        when(exerciseRepository.findById(1)).thenReturn(Optional.of(exercise));
    }

    private AscendExerciseDTO barbellCurlFromExerciseDb() {
        AscendExerciseDTO dto = new AscendExerciseDTO();
        dto.setExerciseId("0001");
        dto.setName("Barbell Curl");
        dto.setImageUrl("https://img.example/1.png");
        dto.setOverview("Overview text");
        dto.setVideoUrl("https://video.example/1.mp4");
        dto.setTargetMuscles(List.of("Biceps"));
        dto.setSecondaryMuscles(List.of("Forearms"));
        dto.setInstructions(List.of("Step one", "Step two"));
        return dto;
    }

    private void assertShowsBarbellCurl(ExerciseDetailsResponse response) {
        assertThat(response.isAvailable()).isTrue();
        assertThat(response.getImageUrl()).isEqualTo("https://img.example/1.png");
        assertThat(response.getOverview()).isEqualTo("Overview text");
        assertThat(response.getVideoUrl()).isEqualTo("https://video.example/1.mp4");
        assertThat(response.getTargetMuscles()).containsExactly("Biceps");
        assertThat(response.getSecondaryMuscles()).containsExactly("Forearms");
        assertThat(response.getInstructions()).containsExactly("Step one", "Step two");
    }

    private void assertUnavailable(ExerciseDetailsResponse response) {
        assertThat(response.isAvailable()).isFalse();
        assertThat(response.getImageUrl()).isNull();
        assertThat(response.getOverview()).isNull();
        assertThat(response.getVideoUrl()).isNull();
        assertThat(response.getInstructions()).isNull();
    }

    // ---- getDetailsForExercise ----

    @Test
    void anUnknownExerciseIdIsUnavailable() {
        when(exerciseRepository.findById(99)).thenReturn(Optional.empty());

        assertUnavailable(service.getDetailsForExercise(99));

        verifyNoInteractions(rateLimiter, ascendApiClient);
    }

    @Test
    void anExerciseNotLinkedToExerciseDbYetIsUnavailable() {
        exerciseLinkedTo(null);

        assertUnavailable(service.getDetailsForExercise(1));

        verifyNoInteractions(rateLimiter, ascendApiClient);
    }

    @Test
    void anEmptyLinkIsTreatedAsNotLinked() {
        exerciseLinkedTo("");

        assertUnavailable(service.getDetailsForExercise(1));

        verifyNoInteractions(rateLimiter, ascendApiClient);
    }

    @Test
    void aLinkedExerciseReturnsTheExerciseDbDetails() {
        exerciseLinkedTo("0001");
        when(rateLimiter.tryConsumeCall()).thenReturn(true);
        when(ascendApiClient.getExerciseById("0001")).thenReturn(Optional.of(barbellCurlFromExerciseDb()));

        assertShowsBarbellCurl(service.getDetailsForExercise(1));
    }

    @Test
    void aSecondRequestWithinTheCacheWindowDoesNotCallTheApiAgain() {
        exerciseLinkedTo("0001");
        when(rateLimiter.tryConsumeCall()).thenReturn(true);
        when(ascendApiClient.getExerciseById("0001")).thenReturn(Optional.of(barbellCurlFromExerciseDb()));

        service.getDetailsForExercise(1);
        ExerciseDetailsResponse second = service.getDetailsForExercise(1);

        assertShowsBarbellCurl(second);
        verify(ascendApiClient, times(1)).getExerciseById("0001");
        verify(rateLimiter, times(1)).tryConsumeCall();
    }

    @Test
    void whenTheRateLimitIsReachedTheApiIsNotCalledAndTheAnswerIsUnavailable() {
        exerciseLinkedTo("0001");
        when(rateLimiter.tryConsumeCall()).thenReturn(false);

        assertUnavailable(service.getDetailsForExercise(1));

        verifyNoInteractions(ascendApiClient);
    }

    @Test
    void aRefusedCallIsNotCachedSoALaterRequestCanSucceed() {
        exerciseLinkedTo("0001");
        when(rateLimiter.tryConsumeCall()).thenReturn(false, true);
        when(ascendApiClient.getExerciseById("0001")).thenReturn(Optional.of(barbellCurlFromExerciseDb()));

        assertUnavailable(service.getDetailsForExercise(1));
        assertShowsBarbellCurl(service.getDetailsForExercise(1));
    }

    @Test
    void anExerciseDbMissIsUnavailable() {
        exerciseLinkedTo("0001");
        when(rateLimiter.tryConsumeCall()).thenReturn(true);
        when(ascendApiClient.getExerciseById("0001")).thenReturn(Optional.empty());

        assertUnavailable(service.getDetailsForExercise(1));
    }

    @Test
    void anApiFailureIsUnavailableAndNeverAnException() {
        exerciseLinkedTo("0001");
        when(rateLimiter.tryConsumeCall()).thenReturn(true);
        when(ascendApiClient.getExerciseById("0001")).thenThrow(new RuntimeException("timeout"));

        assertUnavailable(service.getDetailsForExercise(1));
    }

    // ---- searchForAdminMatching ----

    @Test
    void searchGives429WhenTheRateLimitIsReached() {
        when(rateLimiter.tryConsumeCall()).thenReturn(false);

        BadRequestException error = thrownBadRequest(() -> service.searchForAdminMatching("curl"));

        assertThat(error.getErrorId()).isEqualTo(429);
        assertThat(error.getErrorMessage()).isEqualTo("AscendAPI rate limit reached, try again later.");
        verifyNoInteractions(ascendApiClient);
    }

    @Test
    void searchGives502WhenExerciseDbFails() {
        when(rateLimiter.tryConsumeCall()).thenReturn(true);
        when(ascendApiClient.searchExercises("curl")).thenThrow(new RuntimeException("timeout"));

        BadRequestException error = thrownBadRequest(() -> service.searchForAdminMatching("curl"));

        assertThat(error.getErrorId()).isEqualTo(502);
        assertThat(error.getErrorMessage()).isEqualTo("ExerciseDB is currently unavailable");
    }

    @Test
    void searchReturnsWhatExerciseDbFound() {
        List<ExerciseDbSearchResult> found =
                List.of(new ExerciseDbSearchResult("0001", "Barbell Curl", "https://img.example/1.png"));
        when(rateLimiter.tryConsumeCall()).thenReturn(true);
        when(ascendApiClient.searchExercises("curl")).thenReturn(found);

        assertThat(service.searchForAdminMatching("curl")).isSameAs(found);
    }
}