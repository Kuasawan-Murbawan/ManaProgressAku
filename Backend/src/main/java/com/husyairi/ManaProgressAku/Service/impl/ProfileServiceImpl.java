package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.Profile.GetProfileResponse;
import com.husyairi.ManaProgressAku.DTO.Profile.UpdateProfileRequest;
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
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private Long getCurrentUserId(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        return currentUser.getId();
    }

    @Override
    public GetProfileResponse getProfile(){
        Long currentUserId = getCurrentUserId();
        UserProfile user =  userProfileRepository.findByUserId(currentUserId).orElseThrow(() -> new BadRequestException(
                404,
                "User not found",
                new HashMap<>()
        ));

        return new GetProfileResponse(
                user.getWeightKg(),
                user.getHeightCm(),
                user.getDateOfBirth(),
                user.getGender() );
    }

    @Override
    public GetProfileResponse updateProfile(UpdateProfileRequest request) {

        UserProfile currentUser = userProfileRepository.findByUserId(getCurrentUserId()).orElseThrow(()-> new BadRequestException(
                404,
                "User not found",
                new HashMap<>()
        ));

        // 3. update the new info
        currentUser.setWeightKg(request.getWeightKg());
        currentUser.setHeightCm(request.getHeightCm());
        currentUser.setDateOfBirth(request.getDateOfBirth());
        currentUser.setGender(request.getGender());

        // 4. save the new details
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

    }
}
