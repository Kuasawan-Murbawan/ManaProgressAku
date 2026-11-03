package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.Exercise.GetExerciseResponse;
import com.husyairi.ManaProgressAku.DTO.Exercise.InsertExerciseRequest;
import com.husyairi.ManaProgressAku.DTO.Exercise.InsertExerciseResponse;
import com.husyairi.ManaProgressAku.DTO.Exercise.LinkExerciseDbRequest;
import com.husyairi.ManaProgressAku.DTO.Exercise.UpdateExerciseRequest;
import com.husyairi.ManaProgressAku.Entity.Model.Exercise;
import com.husyairi.ManaProgressAku.Enums.Equipment;
import com.husyairi.ManaProgressAku.ExceptionHandling.BadRequestException;
import com.husyairi.ManaProgressAku.Repository.ActivityRepository;
import com.husyairi.ManaProgressAku.Repository.ExerciseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.husyairi.ManaProgressAku.support.BadRequestAssertions.thrownBadRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceImplTest {

    private static final Equipment FIRST_EQUIPMENT = Equipment.values()[0];
    private static final Equipment LAST_EQUIPMENT = Equipment.values()[Equipment.values().length - 1];

    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ExerciseServiceImpl service;

    private Exercise exercise(int id, String name, String ascendId) {
        Exercise exercise = new Exercise();
        exercise.setExerciseID(id);
        exercise.setExerciseName(name);
        exercise.setExerciseType("1");
        exercise.setAscendExerciseId(ascendId);
        return exercise;
    }

    private InsertExerciseRequest insertRequest(String name, String type, String info,
                                                Boolean bodyweight, Equipment equipment) {
        return new InsertExerciseRequest(name, info, type, bodyweight, equipment);
    }

    private UpdateExerciseRequest updateRequest(Integer id, String name, String type, String info,
                                                Boolean bodyweight, Equipment equipment) {
        return new UpdateExerciseRequest(id, name, info, type, bodyweight, equipment);
    }

    private LinkExerciseDbRequest linkRequest(String ascendExerciseId) {
        LinkExerciseDbRequest request = new LinkExerciseDbRequest();
        request.setAscendExerciseId(ascendExerciseId);
        return request;
    }

    // ---- insertExercise ----

    @Test
    void insertSavesEveryFieldAndKeepsTheBodyweightFlag() {
        when(exerciseRepository.save(any(Exercise.class))).thenAnswer(call -> call.getArgument(0));

        InsertExerciseResponse response = service.insertExercise(
                insertRequest("Push Up", "1", "Chest and triceps", true, FIRST_EQUIPMENT));

        ArgumentCaptor<Exercise> saved = ArgumentCaptor.forClass(Exercise.class);
        verify(exerciseRepository).save(saved.capture());
        assertThat(saved.getValue().getExerciseName()).isEqualTo("Push Up");
        assertThat(saved.getValue().getExerciseType()).isEqualTo("1");
        assertThat(saved.getValue().getGeneralInfo()).isEqualTo("Chest and triceps");
        assertThat(saved.getValue().getIsBodyweight()).isTrue();
        assertThat(saved.getValue().getEquipment()).isEqualTo(FIRST_EQUIPMENT);

        assertThat(response.getExerciseName()).isEqualTo("Push Up");
        assertThat(response.getInfo()).isEqualTo("Chest and triceps");
        assertThat(response.getBodyweight()).isTrue();
        assertThat(response.getEquipment()).isEqualTo(FIRST_EQUIPMENT);
        assertThat(response.getAscendExerciseId()).isNull();
    }

    @Test
    void insertWithoutANameGives400AndSavesNothing() {
        BadRequestException error = thrownBadRequest(
                () -> service.insertExercise(insertRequest(null, "1", "info", false, FIRST_EQUIPMENT)));

        assertThat(error.getErrorId()).isEqualTo(400);
        assertThat(error.getErrorMessage()).isEqualTo("Please fill in all details");
        verify(exerciseRepository, never()).save(any(Exercise.class));
    }

    @Test
    void insertWithoutATypeGives400AndSavesNothing() {
        BadRequestException error = thrownBadRequest(
                () -> service.insertExercise(insertRequest("Push Up", null, "info", false, FIRST_EQUIPMENT)));

        assertThat(error.getErrorId()).isEqualTo(400);
        verify(exerciseRepository, never()).save(any(Exercise.class));
    }

    @Test
    void insertTurnsADatabaseFailureInto400WithTheMessage() {
        when(exerciseRepository.save(any(Exercise.class))).thenThrow(new RuntimeException("duplicate name"));

        BadRequestException error = thrownBadRequest(
                () -> service.insertExercise(insertRequest("Push Up", "1", "info", false, FIRST_EQUIPMENT)));

        assertThat(error.getErrorId()).isEqualTo(400);
        assertThat(error.getErrorMessage()).isEqualTo("duplicate name");
    }

    // ---- getExercise / getAllExercise ----

    @Test
    void getExerciseReturnsTheExerciseWhenItExists() {
        Exercise stored = exercise(5, "Squat", "0042");
        stored.setGeneralInfo("Legs");
        stored.setIsBodyweight(false);
        stored.setEquipment(LAST_EQUIPMENT);
        when(exerciseRepository.findById(5)).thenReturn(Optional.of(stored));

        GetExerciseResponse response = service.getExercise(5);

        assertThat(response.getExerciseName()).isEqualTo("Squat");
        assertThat(response.getInfo()).isEqualTo("Legs");
        assertThat(response.getIsBodyweight()).isFalse();
        assertThat(response.getEquipment()).isEqualTo(LAST_EQUIPMENT);
        assertThat(response.getAscendExerciseId()).isEqualTo("0042");
    }

    @Test
    void getExerciseGives404ForAnUnknownId() {
        when(exerciseRepository.findById(99)).thenReturn(Optional.empty());

        BadRequestException error = thrownBadRequest(() -> service.getExercise(99));

        assertThat(error.getErrorId()).isEqualTo(404);
        assertThat(error.getErrorMessage()).isEqualTo("Exercise ID not found");
    }

    @Test
    void getAllExerciseReturnsWhatTheRepositoryHolds() {
        List<Exercise> all = List.of(exercise(1, "Squat", null), exercise(2, "Curl", null));
        when(exerciseRepository.findAll()).thenReturn(all);

        assertThat(service.getAllExercise()).isEqualTo(all);
    }

    // ---- updateExercise ----

    @Test
    void updateOverwritesTheFieldsButNeverTouchesTheExerciseDbLink() {
        Exercise existing = exercise(5, "Old name", "0001");
        existing.setEquipment(FIRST_EQUIPMENT);
        when(exerciseRepository.findById(5)).thenReturn(Optional.of(existing));

        InsertExerciseResponse response = service.updateExercise(
                updateRequest(5, "New name", "2", "New info", true, LAST_EQUIPMENT));

        ArgumentCaptor<Exercise> saved = ArgumentCaptor.forClass(Exercise.class);
        verify(exerciseRepository).save(saved.capture());
        assertThat(saved.getValue()).isSameAs(existing);
        assertThat(existing.getExerciseName()).isEqualTo("New name");
        assertThat(existing.getExerciseType()).isEqualTo("2");
        assertThat(existing.getGeneralInfo()).isEqualTo("New info");
        assertThat(existing.getIsBodyweight()).isTrue();
        assertThat(existing.getEquipment()).isEqualTo(LAST_EQUIPMENT);
        assertThat(existing.getAscendExerciseId()).isEqualTo("0001");
        assertThat(response.getAscendExerciseId()).isEqualTo("0001");
    }

    @Test
    void updateGives404ForAnUnknownId() {
        when(exerciseRepository.findById(99)).thenReturn(Optional.empty());

        BadRequestException error = thrownBadRequest(
                () -> service.updateExercise(updateRequest(99, "x", "1", "x", false, FIRST_EQUIPMENT)));

        assertThat(error.getErrorId()).isEqualTo(404);
        assertThat(error.getErrorMessage()).isEqualTo("Exercise with ID 99 not found.");
    }

    @Test
    void updateTurnsASaveFailureInto500() {
        when(exerciseRepository.findById(5)).thenReturn(Optional.of(exercise(5, "Squat", null)));
        when(exerciseRepository.save(any(Exercise.class))).thenThrow(new RuntimeException("db down"));

        BadRequestException error = thrownBadRequest(
                () -> service.updateExercise(updateRequest(5, "Squat", "1", "x", false, FIRST_EQUIPMENT)));

        assertThat(error.getErrorId()).isEqualTo(500);
    }

    // ---- linkExerciseToDb ----

    @Test
    void linkStoresTheExerciseDbId() {
        Exercise existing = exercise(5, "Squat", null);
        when(exerciseRepository.findById(5)).thenReturn(Optional.of(existing));

        InsertExerciseResponse response = service.linkExerciseToDb(5, linkRequest("0042"));

        verify(exerciseRepository).save(existing);
        assertThat(existing.getAscendExerciseId()).isEqualTo("0042");
        assertThat(response.getAscendExerciseId()).isEqualTo("0042");
    }

    @Test
    void linkGives404ForAnUnknownExercise() {
        when(exerciseRepository.findById(99)).thenReturn(Optional.empty());

        BadRequestException error = thrownBadRequest(() -> service.linkExerciseToDb(99, linkRequest("0042")));

        assertThat(error.getErrorId()).isEqualTo(404);
        assertThat(error.getErrorMessage()).isEqualTo("Exercise with ID 99 not found");
    }

    @Test
    void linkTurnsASaveFailureInto500() {
        when(exerciseRepository.findById(5)).thenReturn(Optional.of(exercise(5, "Squat", null)));
        when(exerciseRepository.save(any(Exercise.class))).thenThrow(new RuntimeException("db down"));

        BadRequestException error = thrownBadRequest(() -> service.linkExerciseToDb(5, linkRequest("0042")));

        assertThat(error.getErrorId()).isEqualTo(500);
        assertThat(error.getErrorMessage()).isEqualTo("Failed to link exercise");
    }

    // ---- deleteExercise ----

    @Test
    void deleteIsRefusedWith409WhenLoggedWorkoutsUseTheExercise() {
        when(exerciseRepository.findById(5)).thenReturn(Optional.of(exercise(5, "Bench Press", null)));
        when(activityRepository.existsByExerciseID(5)).thenReturn(true);

        BadRequestException error = thrownBadRequest(() -> service.deleteExercise(5));

        assertThat(error.getErrorId()).isEqualTo(409);
        assertThat(error.getErrorMessage()).contains("Bench Press").contains("can't be deleted");
        verify(exerciseRepository, never()).deleteById(any());
    }

    @Test
    void deleteGives404ForAnUnknownId() {
        when(exerciseRepository.findById(99)).thenReturn(Optional.empty());
        // existsByExerciseID is not stubbed on purpose: the mock answers false by default

        BadRequestException error = thrownBadRequest(() -> service.deleteExercise(99));

        assertThat(error.getErrorId()).isEqualTo(404);
        assertThat(error.getErrorMessage()).isEqualTo("Exercise ID 99 not found.");
    }

    @Test
    void deleteGives404ForAnUnknownIdEvenIfStrayActivitiesStillPointAtIt() {
        // Activity.exerciseID is a plain column, not a foreign key, so stray rows are possible.
        when(exerciseRepository.findById(99)).thenReturn(Optional.empty());
        lenient().when(activityRepository.existsByExerciseID(99)).thenReturn(true);

        BadRequestException error = thrownBadRequest(() -> service.deleteExercise(99));

        assertThat(error.getErrorId()).isEqualTo(404);
    }

    @Test
    void deleteRemovesAnUnusedExercise() {
        when(exerciseRepository.findById(5)).thenReturn(Optional.of(exercise(5, "Squat", null)));
        when(activityRepository.existsByExerciseID(5)).thenReturn(false);

        service.deleteExercise(5);

        verify(exerciseRepository).deleteById(5);
    }

    @Test
    void deleteTurnsADatabaseFailureInto500WithTheReason() {
        when(exerciseRepository.findById(5)).thenReturn(Optional.of(exercise(5, "Squat", null)));
        when(activityRepository.existsByExerciseID(5)).thenReturn(false);
        doThrow(new RuntimeException("db down")).when(exerciseRepository).deleteById(5);

        BadRequestException error = thrownBadRequest(() -> service.deleteExercise(5));

        assertThat(error.getErrorId()).isEqualTo(500);
        assertThat(error.getErrorDetails()).containsEntry("error", "db down");
    }
}