package com.husyairi.ManaProgressAku.Controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.husyairi.ManaProgressAku.DTO.User.CustomUserDetails;
import com.husyairi.ManaProgressAku.Entity.Model.User;
import com.husyairi.ManaProgressAku.Enums.Role;
import com.husyairi.ManaProgressAku.Service.impl.UserService;
import com.husyairi.ManaProgressAku.support.WebLayerTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(UserController.class)
class UserControllerTest extends WebLayerTest {

    private static final String FAKE_HASH = "FAKE-HASH-FOR-TEST";

    @MockitoBean
    private UserService userService;

    private static User userWithFakeHash(String email, Role role) {
        User user = new User("Test", email, FAKE_HASH, role);
        user.setId(1L);
        return user;
    }

    private static void assertNoPassword(String body) {
        assertThat(body).doesNotContain(FAKE_HASH).doesNotContain("\"password\"");
    }

    // Regression: this returned the logged-in user's password hash as JSON.
    @Test
    void usersKawe_doesNotReturnThePassword() throws Exception {
        String auth = loggedInAs(Role.USER);
        // The response is the logged-in principal, so the fake hash has to be on that user.
        CustomUserDetails details = new CustomUserDetails(userWithFakeHash("user@test.com", Role.USER));
        when(customUserDetailsService.loadUserByUsername("user@test.com")).thenReturn(details);
        when(jwtService.isTokenValid(auth.substring("Bearer ".length()), details)).thenReturn(true);

        String body = mockMvc.perform(get("/users/kawe").header(AUTHORIZATION, auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@test.com"))
                .andReturn().getResponse().getContentAsString();

        assertNoPassword(body);
    }

    // Regression: this returned the password hash of every user as JSON.
    @Test
    void usersAll_doesNotReturnAnyPassword() throws Exception {
        String auth = loggedInAs(Role.ADMIN);
        when(userService.allUsers()).thenReturn(List.of(
                userWithFakeHash("one@test.com", Role.USER),
                userWithFakeHash("two@test.com", Role.ADMIN)));

        String body = mockMvc.perform(get("/users/all").header(AUTHORIZATION, auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("one@test.com"))
                .andExpect(jsonPath("$[1].email").value("two@test.com"))
                .andReturn().getResponse().getContentAsString();

        assertNoPassword(body);
    }
}
