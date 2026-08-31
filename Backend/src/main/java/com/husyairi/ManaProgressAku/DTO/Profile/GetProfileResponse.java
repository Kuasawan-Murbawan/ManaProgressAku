package com.husyairi.ManaProgressAku.DTO.Profile;

import com.husyairi.ManaProgressAku.Enums.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GetProfileResponse {
    private BigDecimal weightKg;
    private BigDecimal heightCm;
    private LocalDate dateOfBirth;
    private Gender gender;

    public GetProfileResponse(BigDecimal weightKg, BigDecimal heightCm, LocalDate dateOfBirth, Gender gender) {
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