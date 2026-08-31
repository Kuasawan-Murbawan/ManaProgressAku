package com.husyairi.ManaProgressAku.DTO.Profile;

import com.husyairi.ManaProgressAku.Enums.Gender;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Past;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateProfileRequest {

    @DecimalMin(value = "1.0", message = "Weight must be greater than 0")
    @DecimalMax(value = "500.0", message = "Weight seems unrealistic")
    private BigDecimal weightKg;

    @DecimalMin(value = "50.0", message = "Height must be greater than 0")
    @DecimalMax(value = "300.0", message = "Height seems unrealistic")
    private BigDecimal heightCm;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private Gender gender;

    public UpdateProfileRequest() {
    }

    public UpdateProfileRequest(BigDecimal weightKg, BigDecimal heightCm, LocalDate dateOfBirth, Gender gender) {
        this.weightKg = weightKg;
        this.heightCm = heightCm;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public BigDecimal getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(BigDecimal weightKg) {
        this.weightKg = weightKg;
    }

    public BigDecimal getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(BigDecimal heightCm) {
        this.heightCm = heightCm;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}