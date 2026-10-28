package com.husyairi.ManaProgressAku.Service.impl;
import com.husyairi.ManaProgressAku.DTO.User.LoginResponse;
import com.husyairi.ManaProgressAku.DTO.User.LoginUser;
import com.husyairi.ManaProgressAku.DTO.User.RegisterUser;
import com.husyairi.ManaProgressAku.Entity.Model.User;
import com.husyairi.ManaProgressAku.Enums.Role;
import com.husyairi.ManaProgressAku.ExceptionHandling.BadRequestException;
import com.husyairi.ManaProgressAku.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.husyairi.ManaProgressAku.support.BadRequestAssertions.thrownBadRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterUser registration(String name, String email, String password){
        RegisterUser newUser = new RegisterUser();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(password);
        return newUser;
    }

    private LoginUser login(String email, String password){
        LoginUser user = new LoginUser();
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }

    @Test
    void signUpSavesNewUserWithHashedPasswordAndUserRole() {
        when(userRepository.findByEmail("afiq@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("PlainPass1!")).thenReturn("HASHED");
        when(userRepository.save(any(User.class))).thenAnswer(call -> call.getArgument(0));

        User result = authenticationService.signUp(registration("Afiq", "afiq@email.com", "PlainPass1!"));

        ArgumentCaptor<User> saved = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(saved.capture());

        assertThat(saved.getValue().getName()).isEqualTo("Afiq");
        assertThat(saved.getValue().getEmail()).isEqualTo("afiq@email.com");
        assertThat(saved.getValue().getPassword()).isEqualTo("HASHED");
        assertThat(saved.getValue().getRole()).isEqualTo(Role.USER);
        assertThat(result).isSameAs(saved.getValue());
    }

    @Test
    void authenticateReturnsTokenAndExpiry(){
        /*
        handle authenticationManager.authenticaxte -> returns fake Authentication
        authentication.getPrincipal -> returns fake UserDetails
        jwtService.generateToken -> returns String
        jwtService.getExpirationTime -> long

        Work from backwards
        1. LoginResponse(token, jwtService.getExpirationTime())
        - this takes token and expiration time and we can make them up both

        2. String token = jwtService.generateToken(authenticatedUser);
        - needs UserDetails but did not read any of it so can just pass UserDetails.class

        3. UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
        - need Authentication and never reads it so can just pass Authentication.class

        4. Authentication authentication = authenticationManager.authenticate
        - return Authentication

         */

        Authentication authentication = mock(Authentication.class);
        UserDetails authenticatedUser = mock(UserDetails.class);

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(authenticatedUser);
        when(jwtService.generateToken(authenticatedUser)).thenReturn("abc13");
        when(jwtService.getExpirationTime()).thenReturn(360000L);

        // Simulate the real call
        LoginResponse response = authenticationService.authenticateUser(login("afiq@email.com", "PlainPass1"));

        assertThat(response.getToken()).isEqualTo("abc13");
        assertThat(response.getExpiresIn()).isEqualTo(360000L);
  }


    @Test
    void authenticateHandsTheEmailAndPasswordToTheManager() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mock(UserDetails.class));
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);

        authenticationService.authenticateUser(login("afiq@email.com", "PlainPass1!"));

        ArgumentCaptor<Authentication> sent = ArgumentCaptor.forClass(Authentication.class);
        verify(authenticationManager).authenticate(sent.capture());
        assertThat(sent.getValue().getPrincipal()).isEqualTo("afiq@email.com");
        assertThat(sent.getValue().getCredentials()).isEqualTo("PlainPass1!");
    }


}
