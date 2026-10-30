package com.husyairi.ManaProgressAku.Config;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.husyairi.ManaProgressAku.DTO.User.CustomUserDetails;
import com.husyairi.ManaProgressAku.Entity.Model.User;
import com.husyairi.ManaProgressAku.Enums.Role;
import com.husyairi.ManaProgressAku.Service.impl.CustomUserDetailsService;
import com.husyairi.ManaProgressAku.Service.impl.JwtService;
import java.time.Instant;

import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {
    // Fake keys generated only for tests. Safe to commit.
    private static final String SECRET = "DQNXg8dzlNgSO5dzwLfVTzCVGctRs5Z2wVGRlijfOuM=";
    private static final String OTHER_SECRET = "OhHQpxR7jbddQrU5tEX6H3eXf240JdixAQxXYlEWKvY=";
    private static final long ONE_HOUR = 3_600_000L;
    private static final String EMAIL = "gym@test.com";
    private static final String MISSING_HEADER = "Missing or invalid Authorization header";
    private static final String BAD_TOKEN = "Invalid or expired token";

    @Mock
    private CustomUserDetailsService userDetailsService;

    private final ObjectMapper mapper = new ObjectMapper();
    private JwtService jwtService;
    private JwtAuthenticationFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain chain;

    @BeforeEach
    void setUp(){
        SecurityContextHolder.clearContext();
        jwtService = serviceWith(SECRET, ONE_HOUR);
        filter = new JwtAuthenticationFilter(jwtService, userDetailsService);
        request = new MockHttpServletRequest("GET", "/getAllExercise");
        request.setServletPath("/getAllExercises");
        response = new MockHttpServletResponse();
        chain = new MockFilterChain();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // ====== HELPERS =========
    private static JwtService serviceWith(String secret, long expirationMillis){
        JwtService service = new JwtService();
        ReflectionTestUtils.setField(service, "secretKey", secret);
        ReflectionTestUtils.setField(service, "jwtExpiration", expirationMillis);
        return  service;
    }

    private static CustomUserDetails user(Role role, Instant passwordChangedAt){
        User user = new User("Gym Tester", EMAIL, "HASH", role);
        user.setId(7L);
        user.setPasswordChangedAt(passwordChangedAt);
        return new CustomUserDetails(user);
    }

    private void sendWithBearer(String token) throws Exception {
        request.addHeader("Authorization", "Bearer " + token);
        filter.doFilter(request, response, chain);
    }

    // A rejection means: 401, JSON body with the message, the controller never reached, nobody logged in.
    private void assertRejected(String expectedMessage) throws Exception {
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentType()).startsWith("application/json");

        JsonNode body = mapper.readTree(response.getContentAsString());

        assertThat(body.get("status").asInt()).isEqualTo(401);
        assertThat(body.get("message").asText()).isEqualTo(expectedMessage);
        assertThat(chain.getRequest()).as("the request must not reach the controller").isNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    // ======== REJECTED REQUESTS =========
    @Test
    void noAuthorizationHeader_is401() throws Exception {
        filter.doFilter(request, response, chain);

        assertRejected(MISSING_HEADER);
    }

    @Test
    void headerWithoutBearerPrefix_is401() throws Exception {
        request.addHeader("Authorization", "Basic dXNlcjpwYXNz");
        filter.doFilter(request, response, chain);

        assertRejected(MISSING_HEADER);
    }

    @Test
    void garbageToken_is401() throws Exception {
        sendWithBearer("not-a-token");

        assertRejected(BAD_TOKEN);
    }

    @Test
    void emptyBearerToken_is401() throws Exception {
        sendWithBearer("");

        assertRejected(BAD_TOKEN);
    }

    @Test
    void expiredToken_is401() throws Exception {
        String expired = serviceWith(SECRET, -1_000L).generateToken(user(Role.USER, null));

        sendWithBearer(expired);

        assertRejected(BAD_TOKEN);
    }

    @Test
    void tokenSignedWithAnotherKey_is401() throws Exception {
        String forged = serviceWith(OTHER_SECRET, ONE_HOUR).generateToken(user(Role.ADMIN, null));

        sendWithBearer(forged);

        assertRejected(BAD_TOKEN);
    }

}
