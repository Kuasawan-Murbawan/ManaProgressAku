package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.User.LoginUser;
import com.husyairi.ManaProgressAku.DTO.User.RegisterUser;
import com.husyairi.ManaProgressAku.Entity.Model.User;
import com.husyairi.ManaProgressAku.Repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signUp(RegisterUser  newUserInput){
        User user = new User(
                            newUserInput.getName(),
                            newUserInput.getEmail(),
                            passwordEncoder.encode(newUserInput.getPassword()));

        return userRepository.save(user);
    }

    public User authenticateUser(LoginUser userInput) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userInput.getEmail(),
                        userInput.getPassword()
                )
        );

        return userRepository.findByEmail(userInput.getEmail()).orElseThrow();
        }
    }
