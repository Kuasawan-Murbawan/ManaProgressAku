package com.husyairi.ManaProgressAku.Controller;

import com.husyairi.ManaProgressAku.DTO.User.LoginResponse;
import com.husyairi.ManaProgressAku.DTO.User.LoginUser;
import com.husyairi.ManaProgressAku.DTO.User.RegisterUser;
import com.husyairi.ManaProgressAku.Entity.Model.User;
import com.husyairi.ManaProgressAku.Service.impl.AuthenticationService;
import com.husyairi.ManaProgressAku.Service.impl.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService){
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> registerUser(@RequestBody RegisterUser registerUserDetail){
        User registeredUser = authenticationService.signUp(registerUserDetail);

        String jwtToken = jwtService.generateToken(registeredUser);

        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());


        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginUser loginUserDetails){

        User authenticatedUser = authenticationService.authenticateUser(loginUserDetails);
        // authenticatedUser contains User object that has been authenticated

        // generate token
        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
