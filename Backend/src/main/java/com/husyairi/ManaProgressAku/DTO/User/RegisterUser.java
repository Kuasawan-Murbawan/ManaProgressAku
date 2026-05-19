package com.husyairi.ManaProgressAku.DTO.User;

import io.swagger.v3.oas.annotations.media.Schema;

public class RegisterUser {

    @Schema(example = "afiq@email.com", description = "User email address")
    private String email;

    @Schema(example = "passwordForAfiq", description = "User password")
    private String password;

    @Schema(example = "Afiq", description = "User display name")
    private String name;

    // we disable role bc when new user register, they dont have option to have admin privilege. only i have hehe,
    // so need hardcode
//    private String role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}