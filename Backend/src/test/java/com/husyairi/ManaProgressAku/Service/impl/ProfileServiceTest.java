package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.Profile.GetProfileResponse;
import com.husyairi.ManaProgressAku.DTO.Profile.UpdateProfileRequest;
import com.husyairi.ManaProgressAku.DTO.User.ChangePasswordRequest;
import com.husyairi.ManaProgressAku.DTO.User.ChangePasswordResponse;
import com.husyairi.ManaProgressAku.DTO.User.DeleteAccountRequest;
import com.husyairi.ManaProgressAku.Entity.Model.User;
import com.husyairi.ManaProgressAku.Entity.Model.UserProfile;
import com.husyairi.ManaProgressAku.Enums.Gender;
import com.husyairi.ManaProgressAku.Enums.Role;
import com.husyairi.ManaProgressAku.ExceptionHandling.BadRequestException;
import com.husyairi.ManaProgressAku.Repository.UserProfileRepository;
import com.husyairi.ManaProgressAku.Repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.husyairi.ManaProgressAku.support.BadRequestAssertions.thrownBadRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    private static final String EMAIL = "afiq@email.com";

    private static final Long USER_ID = 123L;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserProfileRepository userProfileRepository;
    @Mock
    private UserProfile userProfile;
    @Mock
    private GetProfileResponse getProfileResponse;

    @InjectMocks // we need all the function in this class so use injectmocks
    private ProfileServiceImpl profileService;

    private User user;

    // Make sure login before each test run so that test dont interfere with each other
    @BeforeEach
    void login(){
        user = new User("Afiq", EMAIL, "STORED_HASH", Role.USER);
        user.setId(USER_ID);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(EMAIL, null, List.of()));
    }

    @AfterEach
    void logout(){
        SecurityContextHolder.clearContext();
    }

    private void userExistsInDatabase() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
    }

    private ChangePasswordRequest passwordRequest(String current, String next, String confirm) {
        return new ChangePasswordRequest(current, next, confirm);
    }


    /* =========== TEST LIST =============

    1. Get Profile
        a. null profile
        b. get complete profile

     */

    @Test
    void getProfileReturnNullProfile(){
        /*
            1. userProfileRepository.findByUserId(currentUserId)
            // dependency: userProfileRepository, Long userID
         */

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

        GetProfileResponse response = profileService.getProfile();

        assertThat(response.getHeightCm()).isNull();
        assertThat(response.getWeightKg()).isNull();
        assertThat(response.getGender()).isNull();
        assertThat(response.getDateOfBirth()).isNull();
    }

    @Test
    void getProfileReturnFullProfile(){
        UserProfile simulatedProfile = new UserProfile();
        simulatedProfile.setWeightKg(new BigDecimal("72.50"));
        simulatedProfile.setHeightCm(new BigDecimal("175.00"));
        simulatedProfile.setDateOfBirth(LocalDate.of(1998, 5, 20));
        simulatedProfile.setGender(Gender.FEMALE);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));// we dont care about user
        when(userProfileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(simulatedProfile));

        GetProfileResponse response = profileService.getProfile();

        assertThat(response.getWeightKg()).isEqualTo(new BigDecimal("72.50"));
        assertThat(response.getHeightCm()).isEqualTo(new BigDecimal("175.00"));
        assertThat(response.getDateOfBirth()).isEqualTo(LocalDate.of(1998,5,20));
        assertThat(response.getGender()).isEqualTo(Gender.FEMALE);

    }

    @Test
    void aTokenForAUserWhoNoLongerExistsFailsWithUsernameNotFound() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> profileService.getProfile()).isInstanceOf(UsernameNotFoundException.class);
    }

    // ---- updateProfile ----

    @Test
    void updateProfileCreatesTheRowTheFirstTime() {
        userExistsInDatabase();
        when(userProfileRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        Gender gender = Gender.values()[0];
        UpdateProfileRequest request = new UpdateProfileRequest(
                new BigDecimal("72.5"), new BigDecimal("175.0"), LocalDate.of(1998, 5, 20), gender);

        GetProfileResponse response = profileService.updateProfile(request);

        ArgumentCaptor<UserProfile> saved = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileRepository).save(saved.capture());
        assertThat(saved.getValue().getUser()).isSameAs(user);
        assertThat(saved.getValue().getWeightKg()).isEqualByComparingTo("72.5");
        assertThat(saved.getValue().getHeightCm()).isEqualByComparingTo("175.0");
        assertThat(saved.getValue().getDateOfBirth()).isEqualTo(LocalDate.of(1998, 5, 20));
        assertThat(saved.getValue().getGender()).isEqualTo(gender);
        assertThat(response.getWeightKg()).isEqualByComparingTo("72.5");
        assertThat(response.getGender()).isEqualTo(gender);
    }

    @Test
    void updateProfileChangesTheExistingRowInsteadOfCreatingAnother() {
        userExistsInDatabase();
        UserProfile existing = new UserProfile();
        existing.setWeightKg(new BigDecimal("60.0"));
        existing.setHeightCm(new BigDecimal("170.0"));
        when(userProfileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(existing));
        UpdateProfileRequest request = new UpdateProfileRequest(
                new BigDecimal("65.0"), new BigDecimal("171.0"), LocalDate.of(1998, 5, 20), Gender.values()[0]);

        profileService.updateProfile(request);

        ArgumentCaptor<UserProfile> saved = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileRepository).save(saved.capture());
        assertThat(saved.getValue()).isSameAs(existing);
        assertThat(existing.getWeightKg()).isEqualByComparingTo("65.0");
        assertThat(existing.getHeightCm()).isEqualByComparingTo("171.0");
    }

    @Test
    void updateProfileTurnsADatabaseFailureInto500() {
        userExistsInDatabase();
        when(userProfileRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(userProfileRepository.save(any(UserProfile.class))).thenThrow(new RuntimeException("db down"));
        UpdateProfileRequest request = new UpdateProfileRequest(
                new BigDecimal("65.0"), new BigDecimal("171.0"), LocalDate.of(1998, 5, 20), Gender.values()[0]);

        BadRequestException error = thrownBadRequest(() -> profileService.updateProfile(request));

        assertThat(error.getErrorId()).isEqualTo(500);
        assertThat(error.getErrorMessage()).isEqualTo("db down");
    }

    // ---- deleteAccount ----

    @Test
    void deleteAccountWithTheWrongPasswordGives401AndKeepsTheUser() {
        userExistsInDatabase();
        when(passwordEncoder.matches("wrong", "STORED_HASH")).thenReturn(false);

        BadRequestException error = thrownBadRequest(
                () -> profileService.deleteAccount(new DeleteAccountRequest("wrong")));

        assertThat(error.getErrorId()).isEqualTo(401);
        assertThat(error.getErrorMessage()).isEqualTo("Incorrect password");
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void deleteAccountWithTheRightPasswordDeletesTheUser() {
        userExistsInDatabase();
        when(passwordEncoder.matches("right", "STORED_HASH")).thenReturn(true);

        profileService.deleteAccount(new DeleteAccountRequest("right"));

        verify(userRepository).delete(user);
    }

    // ---- changePassword ----

    @Test
    void changePasswordStoresTheNewHashAndStampsTheTime() {
        userExistsInDatabase();
        when(passwordEncoder.matches("OldPass1!", "STORED_HASH")).thenReturn(true);
        when(passwordEncoder.encode("NewPass1!")).thenReturn("NEW_HASH");

        Instant before = Instant.now();
        ChangePasswordResponse response =
                profileService.changePassword(passwordRequest("OldPass1!", "NewPass1!", "NewPass1!"));
        Instant after = Instant.now();

        assertThat(response.getMessage()).isEqualTo("Password have been changed successfully");
        assertThat(user.getPassword()).isEqualTo("NEW_HASH");
        assertThat(user.getPasswordChangedAt()).isBetween(before, after);
        assertThat(response.getChangedAt()).isEqualTo(user.getPasswordChangedAt());
        verify(userRepository).save(user);
    }

    @Test
    void changePasswordRejectsAWrongCurrentPassword() {
        userExistsInDatabase();
        when(passwordEncoder.matches("nope", "STORED_HASH")).thenReturn(false);

        BadRequestException error = thrownBadRequest(
                () -> profileService.changePassword(passwordRequest("nope", "NewPass1!", "NewPass1!")));

        assertThat(error.getErrorId()).isEqualTo(400);
        assertThat(error.getErrorMessage()).isEqualTo("Password change failed");
        assertThat(error.getErrorDetails()).containsEntry("currentPassword", "Current password is incorrect");
        assertNothingWasChanged();
    }

    @Test
    void changePasswordRejectsANewPasswordEqualToTheCurrentOne() {
        userExistsInDatabase();
        when(passwordEncoder.matches("SamePass1!", "STORED_HASH")).thenReturn(true);

        BadRequestException error = thrownBadRequest(
                () -> profileService.changePassword(passwordRequest("SamePass1!", "SamePass1!", "SamePass1!")));

        assertThat(error.getErrorId()).isEqualTo(400);
        assertThat(error.getErrorDetails())
                .containsEntry("newPassword", "New password cannot be the same as old password");
        assertNothingWasChanged();
    }

    @Test
    void changePasswordRejectsAConfirmationThatDoesNotMatch() {
        userExistsInDatabase();
        when(passwordEncoder.matches("OldPass1!", "STORED_HASH")).thenReturn(true);

        BadRequestException error = thrownBadRequest(
                () -> profileService.changePassword(passwordRequest("OldPass1!", "NewPass1!", "Different1!")));

        assertThat(error.getErrorId()).isEqualTo(400);
        assertThat(error.getErrorDetails()).containsEntry("confirmNewPassword", "Password does not match");
        assertNothingWasChanged();
    }

    @Test
    void changePasswordReportsTheWrongCurrentPasswordFirstWhenSeveralThingsAreWrong() {
        userExistsInDatabase();
        when(passwordEncoder.matches("nope", "STORED_HASH")).thenReturn(false);

        BadRequestException error = thrownBadRequest(
                () -> profileService.changePassword(passwordRequest("nope", "nope", "Different1!")));

        assertThat(error.getErrorDetails()).containsOnlyKeys("currentPassword");
    }

    private void assertNothingWasChanged() {
        assertThat(user.getPassword()).isEqualTo("STORED_HASH");
        assertThat(user.getPasswordChangedAt()).isNull();
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any(User.class));
    }
}