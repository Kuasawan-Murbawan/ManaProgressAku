package com.husyairi.ManaProgressAku.Controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.husyairi.ManaProgressAku.Enums.Role;
import com.husyairi.ManaProgressAku.Service.*;
import com.husyairi.ManaProgressAku.Service.impl.AuthenticationService;
import com.husyairi.ManaProgressAku.Service.impl.UserService;
import com.husyairi.ManaProgressAku.support.WebLayerTest;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

// No controller named: this loads all of them. Every controller needs its service, so each service
// becomes a @MockitoBean below. See the loop under this code block for how to find the rest.
@WebMvcTest
class EndpointSecurityTest extends WebLayerTest {

    @MockitoBean
    private ExerciseService exerciseService;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private ActivityService activityService;

    @MockitoBean
    private ActivitySetService activitySetService;

    @MockitoBean
    private ExerciseDetailsService exerciseDetailsService;

    @MockitoBean
    private ProfileService profileService;

    @MockitoBean
    private SessionService sessionService;

    @MockitoBean
    private UserService userService;

    @Autowired
    @Qualifier("requestMappingHandlerMapping")
    private RequestMappingHandlerMapping handlerMapping;

    // Every endpoint the controllers register, minus login and register (public). Ids are written as 1.
    private static Stream<Arguments> protectedEndpoints() {
        return Stream.of(
                arguments(POST, "/insertSession"),
                arguments(GET, "/session/active"),
                arguments(PATCH, "/finishSession/1"),
                arguments(DELETE, "/deleteSession/1"),
                arguments(GET, "/getUserSessions"),
                arguments(GET, "/sessions/1/details"),
                arguments(GET, "/getAllSessions"),
                arguments(GET, "/getSession/1"),
                arguments(PUT, "/updateSession"),
                arguments(POST, "/insertActivity"),
                arguments(DELETE, "/deleteActivity/1"),
                arguments(GET, "/getActivity/1"),
                arguments(GET, "/sessionActivities/1"),
                arguments(PUT, "/updateActivity"),
                arguments(GET, "/admin/getAllActivity"),
                arguments(GET, "/admin/test"),
                arguments(POST, "/insertSet"),
                arguments(PUT, "/editActivitySet"),
                arguments(PUT, "/editSet/1"),
                arguments(DELETE, "/deleteSet/1"),
                arguments(GET, "/getSet/1"),
                arguments(GET, "/getSetByActivity/1"),
                arguments(GET, "/getAllExercises"),
                arguments(GET, "/getExercise/1"),
                arguments(POST, "/insertExercise"),
                arguments(PUT, "/updateExercise"),
                arguments(DELETE, "/deleteExercise/1"),
                arguments(PUT, "/linkExerciseToDb/1"),
                arguments(GET, "/getExerciseDetails/1"),
                arguments(GET, "/searchExerciseDbMatches"),
                arguments(GET, "/getProfile"),
                arguments(PUT, "/updateProfile"),
                arguments(PUT, "/changepw"),
                arguments(DELETE, "/deleteUser"),
                arguments(GET, "/users/all"),
                arguments(GET, "/users/kawe"),
                arguments(GET, "/users/me"));
    }

    // The admin-only rows, each with a body or parameter the endpoint can read.
// searchExerciseDbMatches has a required request parameter called query.
    private static Stream<Arguments> adminEndpoints() {
        return Stream.of(
                arguments(POST, "/insertExercise", json(
                        "{'exerciseName':'X','generalInfo':'Y','exerciseType':'1',"
                                + "'isBodyweight':false,'equipment':'OTHER'}")),
                arguments(PUT, "/updateExercise", json(
                        "{'exerciseID':1,'exerciseName':'X','generalInfo':'Y','exerciseType':'1',"
                                + "'isBodyweight':false,'equipment':'OTHER'}")),
                arguments(DELETE, "/deleteExercise/1", ""),
                arguments(PUT, "/linkExerciseToDb/1", json("{'ascendExerciseId':'0025'}")),
                arguments(GET, "/searchExerciseDbMatches?query=curl", ""),
                arguments(GET, "/admin/getAllActivity", ""),
                arguments(GET, "/admin/test", ""),
                arguments(GET, "/getAllSessions", ""),
                arguments(GET, "/users/all", ""));
    }

    @ParameterizedTest(name = "{0} {1} without a token is 401")
    @MethodSource("protectedEndpoints")
    void withoutAToken_is401(HttpMethod method, String path) throws Exception {
        mockMvc.perform(request(method, path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @ParameterizedTest(name = "{0} {1} as a normal user is 403")
    @MethodSource("adminEndpoints")
    void adminEndpoint_asANormalUser_is403(HttpMethod method, String path, String body) throws Exception {
        String auth = loggedInAs(Role.USER);

        mockMvc.perform(request(method, path)
                        .header(AUTHORIZATION, auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value(403));
    }

    // Swagger is not part of the web slice, so a 404 is fine. Only 401 and 403 would mean it is blocked.
    @ParameterizedTest
    @ValueSource(strings = {"/v3/api-docs", "/swagger-ui/index.html", "/actuator/health"})
    void swaggerPaths_areNotBlockedBySecurity(String path) throws Exception {
        int status = mockMvc.perform(get(path)).andReturn().getResponse().getStatus();

        assertThat(status).isNotIn(401, 403);
    }

    // The table above can only go stale if this test is red.
    @Test
    void everyEndpointTheAppRegistersIsInTheTable() {
        Set<String> registered = new TreeSet<>();
        handlerMapping.getHandlerMethods().keySet().forEach(info -> {
            for (RequestMethod method : info.getMethodsCondition().getMethods()) {
                for (String pattern : info.getPathPatternsCondition().getPatternValues()) {
                    registered.add(method + " " + pattern.replaceAll("[{][^}]+[}]", "{}"));
                }
            }
        });
        // login and register are public on purpose
        registered.removeIf(entry -> entry.contains(" /auth/"));

        Set<String> covered = new TreeSet<>();
        protectedEndpoints().forEach(row -> {
            Object[] values = row.get();
            covered.add(values[0] + " " + ((String) values[1]).replaceAll("/[0-9]+", "/{}"));
        });

        assertThat(registered).containsExactlyInAnyOrderElementsOf(covered);
    }

    // Only /actuator/health is public. Everything else Actuator offers must still need a token.
    @ParameterizedTest
    @ValueSource(strings = {"/actuator/env", "/actuator/beans", "/actuator/info"})
    void otherActuatorPaths_stayProtected(String path) throws Exception {
        mockMvc.perform(get(path))
                .andExpect(status().isUnauthorized());
    }
}