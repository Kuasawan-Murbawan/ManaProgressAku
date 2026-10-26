package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.User.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtServiceTest {
    private static final String SECRET = "i5d6IrdxfxxRiOfZ7mtaFjMvxbh58JNX73EGXlzB7T0=";
    private static final String OTHER_SECRET = "l5qY0mv1XCWQekhTwY0leW+hJzwsLvcLioPq9hUEUdU=";
    private static final String EMAIL = "user@example.com";
    private static final long ONE_HOUR = 3_600_000L;

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = serviceWith(SECRET, ONE_HOUR);
    }

    // Manually build jwtservice the way Spring does
    private JwtService serviceWith(String secret, long expirationMillis) {
        JwtService service = new JwtService();
        ReflectionTestUtils.setField(service, "secretKey", secret);
        ReflectionTestUtils.setField(service, "jwtExpiration", expirationMillis);
        return service;
    }

    private CustomUserDetails user(String email, Instant passwordChangedAt) {
        CustomUserDetails user = mock(CustomUserDetails.class);
        when(user.getEmail()).thenReturn(email);
        when(user.getUsername()).thenReturn(email);
        when(user.getPasswordChangedAt()).thenReturn(passwordChangedAt);
        return user;
    }

    // 1. Test if the generated token details is the same as given details
    @Test
    void generateToken_putsEmailInSubject() {
        String token = jwtService.generateToken(user(EMAIL, null));

        assertThat(jwtService.extractUsername(token)).isEqualTo(EMAIL);
    }

    // 2. Test if the token is correct that the user hasnt change password
    @Test
    void isTokenValid_sameUserAndNoPasswordChange_isTrue() {
        CustomUserDetails user = user(EMAIL, null);
        String token = jwtService.generateToken(user);

        assertThat(jwtService.isTokenValid(token, user)).isTrue();
    }

    // 3. Test that the token generated using different email will be false
    @Test
    void isTokenValid_differentUser_isFalse() {
        String token = jwtService.generateToken(user(EMAIL, null));

        assertThat(jwtService.isTokenValid(token, user("wrongUser@example.com", null))).isFalse();
    }


    // 4. Test expired token will throw ExpiredJWTException
    @Test
    void expiredToken_throwsExpiredJwtException(){
        // 1. build jwt that has been expired
        JwtService shortLived = serviceWith(SECRET, -1000); // expired 1 second ago

        // 2. generate the token for the user using jwt that has been expired
        String user = shortLived.generateToken(user(EMAIL, null));

        // 3. make sure the excetion are throw
        assertThatThrownBy(()->jwtService.extractUsername(user)).isInstanceOf(ExpiredJwtException .class);
    }

    // 5. Test token after password change will be invalid
    @Test
    void isTokenValid_passwordChangedAfterTokenWasIssued_isFalse(){
        // 1. Create valid jwt
        String token = jwtService.generateToken(user(EMAIL, null));

        // 2. create user with password changed at newer that jwt
        CustomUserDetails afterChangePassword = user(EMAIL, Instant.now().plusSeconds(60));

        // 3. compare the generatedToken and current user
        // 4. isTokenValid will check if token expired AND if token issued before passwordChange
        // 5. will produce T/F
        // 5. Assert that it returns false
        assertThat(jwtService.isTokenValid(token, afterChangePassword)).isFalse();
    }

    // 6. Test that the generated token is AFTER password change
    @Test
    void isTokenValid_passwordChangedBeforeTokenWasIssued_isTrue() {
        // 1. Create user with a passowrdChangedAt now - 60s
        CustomUserDetails user = user(EMAIL, Instant.now().minusSeconds(60));

        // 2. create a token
        String token = jwtService.generateToken(user);

        // 3. Assert its true
        assertThat(jwtService.isTokenValid(token, user)).isTrue();
    }

    // 7. Issuing token & changing password at the same second (500 mili difference)
    @Test
    void isTokenValid_passwordChangedInSameSecondAsToken_isTrue() {

        // 1. Create a token
        String token = jwtService.generateToken(user(EMAIL, null));

        // 2. extract the token to get issuedAt
        Instant issuedAt = jwtService.extractClaim(token, Claims::getIssuedAt).toInstant();

        // 3. Change the passowrd using issuedAt + 500 milis
        CustomUserDetails passwordChangeUser = user(EMAIL, issuedAt.plusMillis(500));

        // 4. assert that is true
        assertThat(jwtService.isTokenValid(token, passwordChangeUser)).isTrue();
    }

    // 8. Changing password after 1 second of generated token
    @Test
    void isTokenValid_passwordChangedOneSecondAfterToken_isFalse() {

        // 1. Create a token
        String token = jwtService.generateToken(user(EMAIL, null));

        // 2. extract the token to get issuedAt
        Instant issuedAt = jwtService.extractClaim(token, Claims::getIssuedAt).toInstant();

        // 3. Change the passowrd using issuedAt + 1 second
        CustomUserDetails passwordChangeUser = user(EMAIL, issuedAt.plusSeconds(1));

        // 4. assert that is true
        assertThat(jwtService.isTokenValid(token, passwordChangeUser)).isFalse();
    }

    // 9. Token signed with another key will be rejected
    @Test
    void tokenSignedWithAnotherKey_isRejected(){
        // 1. Create a jwtservice using other secrect
        JwtService otherJwtService = serviceWith(OTHER_SECRET, ONE_HOUR);

        // 2. create token using that service and current user
        String forgedToken = otherJwtService.generateToken(user(EMAIL, null));

        // 3. throw exception
        assertThatThrownBy(() -> jwtService.extractUsername(forgedToken)).isInstanceOf(JwtException.class);
    }

    @Test
    void garbageToken_throwsMalformedJwtException() {
        assertThatThrownBy(() -> jwtService.extractUsername("not-a-token"))
                .isInstanceOf(MalformedJwtException.class);
    }

    @Test
    void getExpirationTime_returnsTheConfiguredValue() {
        assertThat(jwtService.getExpirationTime()).isEqualTo(ONE_HOUR);
    }

    // Debug
    /*
    @Test
    void debugTokenIsRejected() {
        CustomUserDetails user = user(EMAIL, null);
        String token = jwtService.generateToken(user);

        System.out.println("DEBUG subject      = " + jwtService.extractUsername(token));
        System.out.println("DEBUG getUsername  = " + user.getUsername());
        System.out.println("DEBUG passwordOk   = " + jwtService.isIssuedAfterPasswordChanged(token, user));
        System.out.println("DEBUG expiresAt    = " + jwtService.extractClaim(token, Claims::getExpiration));
        System.out.println("DEBUG now          = " + new java.util.Date());
    }
    */

}
