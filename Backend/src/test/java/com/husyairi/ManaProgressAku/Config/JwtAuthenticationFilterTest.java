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
class JwtAuthenticationFilterTest {

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
    void setUp() {
        SecurityContextHolder.clearContext();
        jwtService = serviceWith(SECRET, ONE_HOUR);
        filter = new JwtAuthenticationFilter(jwtService, userDetailsService);
        request = new MockHttpServletRequest("GET", "/getAllExercises");
        request.setServletPath("/getAllExercises");
        response = new MockHttpServletResponse();
        chain = new MockFilterChain();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // ---- helpers ----

    private static JwtService serviceWith(String secret, long expirationMillis) {
        JwtService service = new JwtService();
        ReflectionTestUtils.setField(service, "secretKey", secret);
        ReflectionTestUtils.setField(service, "jwtExpiration", expirationMillis);
        return service;
    }

    private static CustomUserDetails user(Role role, Instant passwordChangedAt) {
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

    // ---- rejected requests ----

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

    // A deleted account still holds a perfectly signed token.
    @Test
    void tokenForADeletedUser_is401() throws Exception {
        String token = jwtService.generateToken(user(Role.USER, null));
        when(userDetailsService.loadUserByUsername(EMAIL))
                .thenThrow(new UsernameNotFoundException("gone"));

        sendWithBearer(token);

        assertRejected(BAD_TOKEN);
    }

    // Changing the password must log out every older token (v1.5.0).
    @Test
    void tokenIssuedBeforeAPasswordChange_is401() throws Exception {
        String token = jwtService.generateToken(user(Role.USER, null));
        CustomUserDetails afterChange = user(Role.USER, Instant.now().plusSeconds(60));
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(afterChange);

        sendWithBearer(token);

        assertRejected(BAD_TOKEN);
    }

    // ---- accepted requests ----

    @Test
    void validUserToken_letsTheRequestThroughAsROLE_USER() throws Exception {
        CustomUserDetails details = user(Role.USER, null);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(details);

        sendWithBearer(jwtService.generateToken(details));

        assertThat(chain.getRequest()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(200);
        Authentication login = SecurityContextHolder.getContext().getAuthentication();
        assertThat(login).isNotNull();
        assertThat(login.getName()).isEqualTo(EMAIL);
        assertThat(login.getAuthorities()).extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");
    }

    @Test
    void validAdminToken_letsTheRequestThroughAsROLE_ADMIN() throws Exception {
        CustomUserDetails details = user(Role.ADMIN, null);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(details);

        sendWithBearer(jwtService.generateToken(details));

        assertThat(chain.getRequest()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_ADMIN");
    }

    // Login, register and Swagger must work without any token.
    @ParameterizedTest
    @ValueSource(strings = {"/auth/login", "/auth/signup", "/swagger-ui/index.html", "/v3/api-docs", "/actuator/health"})
    void publicPaths_passWithoutAToken(String path) throws Exception {
        request.setServletPath(path);

        filter.doFilter(request, response, chain);

        assertThat(chain.getRequest()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEmpty();
    }

}