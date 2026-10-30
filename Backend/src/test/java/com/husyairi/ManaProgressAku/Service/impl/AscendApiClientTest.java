package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.Exercise.External.AscendExerciseDTO;
import com.husyairi.ManaProgressAku.DTO.Exercise.External.ExerciseDbSearchResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class AscendApiClientTest {

    private static final String BASE_URL = "http://localhost:9/api/v1";

    private MockRestServiceServer server;
    private AscendApiClient client;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        client = new AscendApiClient(restTemplate, BASE_URL);
    }

    @AfterEach
    void everyExpectedRequestWasActuallyMade() {
        server.verify();
    }

    // ---- getExerciseById ----

    @Test
    void getExerciseByIdReturnsTheExerciseInsideTheDataField() {
        server.expect(requestTo(BASE_URL + "/exercises/0001"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        "{\"success\": true, \"data\": {\"exerciseId\": \"0001\", \"name\": \"Barbell Curl\","
                                + " \"imageUrl\": \"https://img.example/1.png\", \"overview\": \"Overview\","
                                + " \"videoUrl\": \"https://video.example/1.mp4\", \"targetMuscles\": [\"Biceps\"],"
                                + " \"secondaryMuscles\": [\"Forearms\"], \"instructions\": [\"Step one\"],"
                                + " \"somethingNew\": \"ignored\"}}",
                        MediaType.APPLICATION_JSON));

        Optional<AscendExerciseDTO> result = client.getExerciseById("0001");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Barbell Curl");
        assertThat(result.get().getTargetMuscles()).containsExactly("Biceps");
        assertThat(result.get().getInstructions()).containsExactly("Step one");
    }

    @Test
    void getExerciseByIdReturnsEmptyWhenExerciseDbAnswers404() {
        server.expect(requestTo(BASE_URL + "/exercises/missing")).andRespond(withResourceNotFound());

        assertThat(client.getExerciseById("missing")).isEmpty();
    }

    @Test
    void getExerciseByIdReturnsEmptyWhenThereIsNoDataField() {
        server.expect(requestTo(BASE_URL + "/exercises/0001"))
                .andRespond(withSuccess("{\"success\": true, \"data\": null}", MediaType.APPLICATION_JSON));

        assertThat(client.getExerciseById("0001")).isEmpty();
    }

    @Test
    void getExerciseByIdRethrowsServerErrorsSoCallersCanReactToThem() {
        server.expect(requestTo(BASE_URL + "/exercises/0001")).andRespond(withServerError());

        assertThatThrownBy(() -> client.getExerciseById("0001")).isInstanceOf(RestClientException.class);
    }

    // ---- searchExercises ----

    @Test
    void searchReturnsTheListInsideTheDataField() {
        server.expect(requestTo(BASE_URL + "/exercises/search?search=curl"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        "{\"data\": [{\"exerciseId\": \"0001\", \"name\": \"Barbell Curl\","
                                + " \"imageUrl\": \"https://img.example/1.png\"},"
                                + " {\"exerciseId\": \"0002\", \"name\": \"Hammer Curl\", \"imageUrl\": null}]}",
                        MediaType.APPLICATION_JSON));

        List<ExerciseDbSearchResult> results = client.searchExercises("curl");

        assertThat(results).hasSize(2);
        assertThat(results.get(0).getExerciseId()).isEqualTo("0001");
        assertThat(results.get(1).getName()).isEqualTo("Hammer Curl");
    }

    @Test
    void searchSendsATermWithASpaceAsOneQueryParameter() {
        server.expect(requestTo(startsWith(BASE_URL + "/exercises/search")))
                // queryParam matches the raw (still-encoded) value from the URI
                .andExpect(queryParam("search", "bicep%20curl"))
                .andRespond(withSuccess("{\"data\": []}", MediaType.APPLICATION_JSON));

        assertThat(client.searchExercises("bicep curl")).isEmpty();
    }

    @Test
    void searchReturnsAnEmptyListWhenThereIsNoDataField() {
        server.expect(requestTo(BASE_URL + "/exercises/search?search=curl"))
                .andRespond(withSuccess("{\"data\": null}", MediaType.APPLICATION_JSON));

        assertThat(client.searchExercises("curl")).isEmpty();
    }

    @Test
    void searchRethrowsServerErrors() {
        server.expect(requestTo(BASE_URL + "/exercises/search?search=curl")).andRespond(withServerError());

        assertThatThrownBy(() -> client.searchExercises("curl")).isInstanceOf(RestClientException.class);
    }

    // ---- isLive ----

    @Test
    void isLiveIsTrueWhenTheLivenessEndpointAnswers() {
        server.expect(requestTo(BASE_URL + "/liveness")).andRespond(withSuccess("ok", MediaType.TEXT_PLAIN));

        assertThat(client.isLive()).isTrue();
    }

    @Test
    void isLiveIsFalseWhenTheLivenessEndpointFails() {
        server.expect(requestTo(BASE_URL + "/liveness")).andRespond(withServerError());

        assertThat(client.isLive()).isFalse();
    }
}