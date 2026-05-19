package com.husyairi.ManaProgressAku.DTO.User;

import io.swagger.v3.oas.annotations.media.Schema;

public class LoginResponse {

    @Schema(example = "2yHIYAUHUOyh78GAyui..")
    private String token;

    @Schema(example = "360000", description = "Token expiry time in milliseconds")
    private long expiresIn;

    public LoginResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
