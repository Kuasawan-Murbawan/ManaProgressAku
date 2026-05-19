package com.husyairi.ManaProgressAku.Controller;

import com.husyairi.ManaProgressAku.DTO.User.CustomUserDetails;
import com.husyairi.ManaProgressAku.DTO.User.LoginResponse;
import com.husyairi.ManaProgressAku.DTO.User.LoginUser;
import com.husyairi.ManaProgressAku.DTO.User.RegisterUser;
import com.husyairi.ManaProgressAku.Entity.Model.User;
import com.husyairi.ManaProgressAku.Service.impl.AuthenticationService;
import com.husyairi.ManaProgressAku.Service.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Login and Registration APIs")
@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService){
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @Operation(
            summary = "Register User",
            description = "Registers a user using name, email and password, then returns a JWT token."
    )
    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> registerUser(@RequestBody RegisterUser registerUserDetail){
        User registeredUser = authenticationService.signUp(registerUserDetail);

//        String jwtToken = jwtService.generateToken((UserDetails) registeredUser);
        String jwtToken =jwtService.generateToken(new CustomUserDetails(registeredUser));

        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());


        return ResponseEntity.ok(loginResponse);
    }

    @Operation(
            summary = "Login User",
            description = "Authenticates a user using email and password, then returns a JWT token."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginUser loginUserDetails){

        UserDetails authenticatedUser = authenticationService.authenticateUser(loginUserDetails);
        // authenticatedUser contains User object that has been authenticated

        // generate token
        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
