package com.husyairi.ManaProgressAku.Service;

import com.husyairi.ManaProgressAku.DTO.Profile.GetProfileResponse;
import com.husyairi.ManaProgressAku.DTO.Profile.UpdateProfileRequest;
import com.husyairi.ManaProgressAku.DTO.User.DeleteAccountRequest;

public interface ProfileService {
    GetProfileResponse getProfile();
    GetProfileResponse updateProfile(UpdateProfileRequest request);
    void deleteAccount(DeleteAccountRequest request);
}
