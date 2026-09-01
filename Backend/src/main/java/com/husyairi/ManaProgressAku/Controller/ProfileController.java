package com.husyairi.ManaProgressAku.Controller;

import com.husyairi.ManaProgressAku.DTO.Profile.GetProfileResponse;
import com.husyairi.ManaProgressAku.DTO.Profile.UpdateProfileRequest;
import com.husyairi.ManaProgressAku.DTO.User.DeleteAccountRequest;
import com.husyairi.ManaProgressAku.ExceptionHandling.ApiSuccessResponse;
import com.husyairi.ManaProgressAku.Service.ExerciseService;
import com.husyairi.ManaProgressAku.Service.ProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Profile", description = "CRUD for user profile")
@CrossOrigin("*")
@RestController
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }

    @GetMapping("/getProfile")
    public ResponseEntity<ApiSuccessResponse<GetProfileResponse>> getProfile(){
        GetProfileResponse data = profileService.getProfile();

        ApiSuccessResponse<GetProfileResponse> response = new ApiSuccessResponse<>(
                "Profile fetched successfully",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<ApiSuccessResponse<GetProfileResponse>> updateProfile(@RequestBody UpdateProfileRequest request){
        GetProfileResponse data = profileService.updateProfile(request);

        ApiSuccessResponse<GetProfileResponse> response = new ApiSuccessResponse<>(
                "Profile updated successfully",
                data
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteAccount(@RequestBody DeleteAccountRequest request){
        profileService.deleteAccount(request);

        return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
    }
}
