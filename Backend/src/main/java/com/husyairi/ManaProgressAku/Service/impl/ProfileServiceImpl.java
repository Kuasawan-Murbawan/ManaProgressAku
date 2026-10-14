package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.Profile.GetProfileResponse;
import com.husyairi.ManaProgressAku.DTO.Profile.UpdateProfileRequest;
import com.husyairi.ManaProgressAku.DTO.User.ChangePasswordRequest;
import com.husyairi.ManaProgressAku.DTO.User.ChangePasswordResponse;
import com.husyairi.ManaProgressAku.DTO.User.DeleteAccountRequest;
import com.husyairi.ManaProgressAku.Entity.Model.User;
import com.husyairi.ManaProgressAku.Entity.Model.UserProfile;
import com.husyairi.ManaProgressAku.ExceptionHandling.BadRequestException;
import com.husyairi.ManaProgressAku.Repository.UserProfileRepository;
import com.husyairi.ManaProgressAku.Repository.UserRepository;
import com.husyairi.ManaProgressAku.Service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

    }

    private Long getCurrentUserId(){
        return getCurrentUser().getId();
    }

    @Override
    public GetProfileResponse getProfile(){
        Long currentUserId = getCurrentUserId();
        UserProfile user = userProfileRepository.findByUserId(currentUserId).orElse(null);

        if (user == null) {
            return new GetProfileResponse(null, null, null, null);
        }

        return new GetProfileResponse(
                user.getWeightKg(),
                user.getHeightCm(),
                user.getDateOfBirth(),
                user.getGender());
    }

    @Override
    public GetProfileResponse updateProfile(UpdateProfileRequest request) {

        // 1. fetched current user's profile
        UserProfile currentUser = userProfileRepository.findByUserId(getCurrentUserId()).orElse(null);


        if (currentUser == null) {
            currentUser = new UserProfile();
            currentUser.setUser(getCurrentUser());
        }

        // 2. update the new info
        currentUser.setWeightKg(request.getWeightKg());
        currentUser.setHeightCm(request.getHeightCm());
        currentUser.setDateOfBirth(request.getDateOfBirth());
        currentUser.setGender(request.getGender());

        // 3. save the new details
        try{
            userProfileRepository.save(currentUser);
        }catch(Exception e){
            throw new BadRequestException(500, e.getMessage(), new HashMap<>());
        }

        return new GetProfileResponse(
                currentUser.getWeightKg(),
                currentUser.getHeightCm(),
                currentUser.getDateOfBirth(),
                currentUser.getGender()
        );
    }

    @Override
    public void deleteAccount(DeleteAccountRequest request) {
        User currentUser = getCurrentUser();

        if(!passwordEncoder.matches(request.getPassword(), currentUser.getPassword())){
            throw new BadRequestException(401, "Incorrect password", new HashMap<>());
        }

        userRepository.delete(currentUser);
    }

    @Override
    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
        User currentUser = getCurrentUser();

        /* Return false if
            1. current password is wrong
            2. new password and confirm new password is different
            3. current and new password is the same
        */

        // Use the hashmap to handle each request error
        if(!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword()) ){
            throw new BadRequestException(400, "Password change failed",
                    Map.of("currentPassword", "Current password is incorrect"));
        }
        if(request.getCurrentPassword().equals(request.getNewPassword())){
            throw new BadRequestException(400, "Password change failed",
                    Map.of("newPassword", "New password cannot be the same as old password"));
        }
        if(!request.getNewPassword().equals(request.getConfirmNewPassword())
        ){
            throw new BadRequestException(400, "Password change failed",
                    Map.of("confirmNewPassword", "Password does not match"));
        }

        Instant now = Instant.now();
        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        currentUser.setPasswordChangedAt(now);
        userRepository.save(currentUser);

        return new ChangePasswordResponse("Password have been changed successfully", now);
    }
}
