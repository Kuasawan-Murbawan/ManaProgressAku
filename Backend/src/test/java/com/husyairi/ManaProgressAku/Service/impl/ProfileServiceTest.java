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
        user = new User("Afiq", EMAIL, "password", Role.USER);
        user.setId(USER_ID);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(EMAIL, null, List.of()));
    }

    @AfterEach
    void logout(){
        SecurityContextHolder.clearContext();
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
}