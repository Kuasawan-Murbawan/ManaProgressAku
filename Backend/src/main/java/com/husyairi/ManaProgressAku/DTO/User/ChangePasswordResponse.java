package com.husyairi.ManaProgressAku.DTO.User;

import java.time.LocalDateTime;

public class ChangePasswordResponse {
    private String message;
    private LocalDateTime changedAt;

    public ChangePasswordResponse(String message, LocalDateTime changedAt) {
        this.message = message;
        this.changedAt = changedAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }
}
