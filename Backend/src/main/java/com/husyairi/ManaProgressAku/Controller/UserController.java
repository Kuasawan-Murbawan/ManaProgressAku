package com.husyairi.ManaProgressAku.Controller;

import com.husyairi.ManaProgressAku.DTO.User.CustomUserDetails;
import com.husyairi.ManaProgressAku.DTO.User.UserProfile;
import com.husyairi.ManaProgressAku.Entity.Model.User;
import com.husyairi.ManaProgressAku.Service.impl.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/kawe")
    public ResponseEntity<UserDetails> authenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails currentUser = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
     }

    @GetMapping("/me")
    public ResponseEntity<String> me() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(user.getName());
    }



    @GetMapping("/all")
    public ResponseEntity<List<User>> allUsers(){

        System.out.println("DIsplaying all users..");
        List<User> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }

}
