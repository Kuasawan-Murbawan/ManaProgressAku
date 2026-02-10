package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.User.LoginUser;
import com.husyairi.ManaProgressAku.DTO.User.RegisterUser;
import com.husyairi.ManaProgressAku.Entity.Model.User;
import com.husyairi.ManaProgressAku.Enums.Role;
import com.husyairi.ManaProgressAku.ExceptionHandling.BadRequestException;
import com.husyairi.ManaProgressAku.Repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

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

        String newUserEmail = newUserInput.getEmail();

        if(userRepository.findByEmail(newUserEmail).isPresent()){
            throw new BadRequestException(400, "Email already registered", new HashMap<>());
        }

        User user = new User(
                            newUserInput.getName(),
                            newUserInput.getEmail(),
                            passwordEncoder.encode(newUserInput.getPassword()),
                            Role.USER);


        return userRepository.save(user);
    }

    public User authenticateUser(LoginUser userInput) {

        /*
        Looks up user email in database and compares with hashed password
         */
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userInput.getEmail(),
                        userInput.getPassword()
                )
        );

        // If success, find the email in database, this will return User object
        return userRepository.findByEmail(userInput.getEmail()).orElseThrow();
        }
    }
