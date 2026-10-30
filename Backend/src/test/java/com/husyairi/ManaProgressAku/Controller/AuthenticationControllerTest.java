package com.husyairi.ManaProgressAku.Controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.husyairi.ManaProgressAku.DTO.User.LoginResponse;
import com.husyairi.ManaProgressAku.DTO.User.LoginUser;
import com.husyairi.ManaProgressAku.DTO.User.RegisterUser;
import com.husyairi.ManaProgressAku.Entity.Model.User;
import com.husyairi.ManaProgressAku.Enums.Role;
import com.husyairi.ManaProgressAku.ExceptionHandling.BadRequestException;
import com.husyairi.ManaProgressAku.Service.impl.AuthenticationService;
import com.husyairi.ManaProgressAku.support.WebLayerTest;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/*
    Login and register are the only public endpoints, so this class proves three things:
    1. They work without a token, the JSON fields reach the service, and a broken body gives the
    400 error body (the v1.4.0 bug where validation errors came back as 401).
    2. AuthenticationService is faked, so no password hashing or database is involved.
    3. The fake JwtService comes from the base class.
 */

@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest extends WebLayerTest {

    @MockitoBean
    private AuthenticationService authenticationService;

    private static final String SIGNUP_BODY =
            json("{'name':'Gym Tester','email':'gym@test.com','password':'secret123'}");
    private static final String LOGIN_BODY =
            json("{'email':'gym@test.com','password':'secret123'}");

    @Test
    void signup_validBody_returns201WithTokenAndExpiry() throws Exception {
        when(authenticationService.signUp(any(RegisterUser.class)))
                .thenReturn(new User("Gym Tester", "gym@test.com", "HASH", Role.USER));
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("signup-token");
        when(jwtService.getExpirationTime()).thenReturn(3_600_000L);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(SIGNUP_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("signup-token"))
                .andExpect(jsonPath("$.expiresIn").value(3_600_000));
    }

    @Test
    void signup_bindsNameEmailAndPasswordFromTheJson() throws Exception {
        when(authenticationService.signUp(any(RegisterUser.class)))
                .thenReturn(new User("Gym Tester", "gym@test.com", "HASH", Role.USER));

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(SIGNUP_BODY))
                .andExpect(status().isCreated());

        ArgumentCaptor<RegisterUser> sent = ArgumentCaptor.forClass(RegisterUser.class);
        verify(authenticationService).signUp(sent.capture());
        assertThat(sent.getValue().getName()).isEqualTo("Gym Tester");
        assertThat(sent.getValue().getEmail()).isEqualTo("gym@test.com");
        assertThat(sent.getValue().getPassword()).isEqualTo("secret123");
    }

    @Test
    void signup_malformedJson_returns400AndNeverReachesTheService() throws Exception {
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("{'name':")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.errorMessage").value("Malformed request body"));

        verifyNoInteractions(authenticationService);
    }

    @Test
    void login_validBody_returns200WithTheToken() throws Exception {
        when(authenticationService.authenticateUser(any(LoginUser.class)))
                .thenReturn(new LoginResponse("login-token", 3_600_000L));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LOGIN_BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("login-token"))
                .andExpect(jsonPath("$.expiresIn").value(3_600_000));
    }

    @Test
    void login_bindsEmailAndPasswordFromTheJson() throws Exception {
        when(authenticationService.authenticateUser(any(LoginUser.class)))
                .thenReturn(new LoginResponse("login-token", 3_600_000L));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LOGIN_BODY))
                .andExpect(status().isOk());

        ArgumentCaptor<LoginUser> sent = ArgumentCaptor.forClass(LoginUser.class);
        verify(authenticationService).authenticateUser(sent.capture());
        assertThat(sent.getValue().getEmail()).isEqualTo("gym@test.com");
        assertThat(sent.getValue().getPassword()).isEqualTo("secret123");
    }

    // Stand-in for whatever the real service throws on a wrong password: the handler must turn it into this body.
    @Test
    void login_wrongPassword_returns401WithTheErrorBody() throws Exception {
        when(authenticationService.authenticateUser(any(LoginUser.class)))
                .thenThrow(new BadRequestException(401, "Invalid email or password", Map.of()));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LOGIN_BODY))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value(401))
                .andExpect(jsonPath("$.errorMessage").value("Invalid email or password"))
                .andExpect(jsonPath("$.errorDetails").isEmpty());
    }

    @Test
    void login_malformedJson_returns400() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("{'email':")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Malformed request body"));

        verifyNoInteractions(authenticationService);
    }

    @Test
    void login_noBodyAtAll_returns400() throws Exception {
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400));

        verifyNoInteractions(authenticationService);
    }
}