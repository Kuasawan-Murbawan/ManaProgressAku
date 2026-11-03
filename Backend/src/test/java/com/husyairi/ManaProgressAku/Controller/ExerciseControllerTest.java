package com.husyairi.ManaProgressAku.Controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.husyairi.ManaProgressAku.DTO.Exercise.GetExerciseResponse;
import com.husyairi.ManaProgressAku.DTO.Exercise.InsertExerciseRequest;
import com.husyairi.ManaProgressAku.DTO.Exercise.InsertExerciseResponse;
import com.husyairi.ManaProgressAku.DTO.Exercise.UpdateExerciseRequest;
import com.husyairi.ManaProgressAku.Entity.Model.Exercise;
import com.husyairi.ManaProgressAku.Enums.Equipment;
import com.husyairi.ManaProgressAku.Enums.Role;
import com.husyairi.ManaProgressAku.ExceptionHandling.BadRequestException;
import com.husyairi.ManaProgressAku.Service.ExerciseService;
import com.husyairi.ManaProgressAku.support.WebLayerTest;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(ExerciseController.class)
class ExerciseControllerTest extends WebLayerTest {

    @MockitoBean
    private ExerciseService exerciseService;

    private static final String INSERT_BODY = json(
            "{'exerciseName':'Preacher Curl','generalInfo':'Isolates the biceps',"
                    + "'exerciseType':'1','isBodyweight':true,'equipment':'BARBELL'}");

    private static final String UPDATE_BODY = json(
            "{'exerciseID':5,'exerciseName':'Preacher Curl','generalInfo':'Isolates the biceps',"
                    + "'exerciseType':'1','isBodyweight':false,'equipment':'DUMBBELL'}");

    @Test
    void insertExercise_asAdmin_returns201AndWrapsTheResult() throws Exception {
        String auth = loggedInAs(Role.ADMIN);
        when(exerciseService.insertExercise(any(InsertExerciseRequest.class)))
                .thenReturn(new InsertExerciseResponse(
                        5, "Preacher Curl", "1", "Isolates the biceps", true, Equipment.BARBELL, null));

        mockMvc.perform(post("/insertExercise")
                        .header(AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(INSERT_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Exercise inserted successfully"))
                .andExpect(jsonPath("$.data.exerciseID").value(5))
                .andExpect(jsonPath("$.data.exerciseName").value("Preacher Curl"));
    }

    // Regression: the isBodyweight toggle was silently dropped by a getter/setter naming mismatch.
    @Test
    void insertExercise_bindsIsBodyweightAndEquipmentFromTheJson() throws Exception {
        String auth = loggedInAs(Role.ADMIN);

        mockMvc.perform(post("/insertExercise")
                        .header(AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(INSERT_BODY))
                .andExpect(status().isCreated());

        ArgumentCaptor<InsertExerciseRequest> sent = ArgumentCaptor.forClass(InsertExerciseRequest.class);
        verify(exerciseService).insertExercise(sent.capture());
        assertThat(sent.getValue().getExerciseName()).isEqualTo("Preacher Curl");
        assertThat(sent.getValue().getBodyweight()).isTrue();
        assertThat(sent.getValue().getEquipment()).isEqualTo(Equipment.BARBELL);
    }

    @Test
    void insertExercise_malformedJson_returns400AndNeverReachesTheService() throws Exception {
        String auth = loggedInAs(Role.ADMIN);

        mockMvc.perform(post("/insertExercise")
                        .header(AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("{'exerciseName':")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.errorMessage").value("Malformed request body"))
                .andExpect(jsonPath("$.errorDetails").isEmpty());

        verifyNoInteractions(exerciseService);
    }

    @Test
    void insertExercise_unknownEquipmentValue_returns400() throws Exception {
        String auth = loggedInAs(Role.ADMIN);

        mockMvc.perform(post("/insertExercise")
                        .header(AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("{'exerciseName':'X','equipment':'SPACESHIP'}")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Malformed request body"));

        verifyNoInteractions(exerciseService);
    }

    @Test
    void updateExercise_asAdmin_returns200AndBindsTheFields() throws Exception {
        String auth = loggedInAs(Role.ADMIN);

        mockMvc.perform(put("/updateExercise")
                        .header(AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UPDATE_BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Exercise updated successfully"));

        ArgumentCaptor<UpdateExerciseRequest> sent = ArgumentCaptor.forClass(UpdateExerciseRequest.class);
        verify(exerciseService).updateExercise(sent.capture());
        assertThat(sent.getValue().getExerciseID()).isEqualTo(5);
        assertThat(sent.getValue().getIsBodyweight()).isFalse();
        assertThat(sent.getValue().getEquipment()).isEqualTo(Equipment.DUMBBELL);
    }

    @Test
    void getExercise_asUser_returns200WithTheExerciseFields() throws Exception {
        String auth = loggedInAs(Role.USER);
        when(exerciseService.getExercise(5)).thenReturn(new GetExerciseResponse(
                "Preacher Curl", "1", "Isolates the biceps", true, Equipment.BARBELL, "0025"));

        mockMvc.perform(get("/getExercise/5").header(AUTHORIZATION, auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Exercise fetched successfully"))
                .andExpect(jsonPath("$.data.exerciseName").value("Preacher Curl"))
                .andExpect(jsonPath("$.data.isBodyweight").value(true))
                .andExpect(jsonPath("$.data.equipment").value("BARBELL"))
                .andExpect(jsonPath("$.data.ascendExerciseId").value("0025"));
    }

    @Test
    void getExercise_unknownId_returns404WithTheErrorBody() throws Exception {
        String auth = loggedInAs(Role.USER);
        when(exerciseService.getExercise(99))
                .thenThrow(new BadRequestException(404, "Exercise not found", Map.of()));

        mockMvc.perform(get("/getExercise/99").header(AUTHORIZATION, auth))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value("Exercise not found"));
    }

    @Test
    void getAllExercises_asUser_returns200WithTheList() throws Exception {
        String auth = loggedInAs(Role.USER);
        when(exerciseService.getAllExercise()).thenReturn(List.of(
                new Exercise("Bench Press", "1", "Chest", false, Equipment.BARBELL),
                new Exercise("Squat", "2", "Legs", false, Equipment.BARBELL)));

        mockMvc.perform(get("/getAllExercises").header(AUTHORIZATION, auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("All exercises fetched successfully"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].exerciseName").value("Bench Press"))
                .andExpect(jsonPath("$.data[1].exerciseName").value("Squat"));
    }

    @Test
    void deleteExercise_asAdmin_returns200AndCallsTheService() throws Exception {
        String auth = loggedInAs(Role.ADMIN);

        mockMvc.perform(delete("/deleteExercise/5").header(AUTHORIZATION, auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Exercise deleted successfully"));

        verify(exerciseService).deleteExercise(5);
    }

    // Regression: an exercise used in workouts must not be deletable (v1.5.0). The service throws, the handler maps it.
    @Test
    void deleteExercise_exerciseInUse_returns409() throws Exception {
        String auth = loggedInAs(Role.ADMIN);
        doThrow(new BadRequestException(409, "Exercise is used in workouts", Map.of()))
                .when(exerciseService).deleteExercise(5);

        mockMvc.perform(delete("/deleteExercise/5").header(AUTHORIZATION, auth))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value(409));
    }
}