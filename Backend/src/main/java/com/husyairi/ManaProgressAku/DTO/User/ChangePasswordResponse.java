package com.husyairi.ManaProgressAku.DTO.User;

import java.time.Instant;
import java.time.LocalDateTime;

public class ChangePasswordResponse {
    private String message;
    private Instant changedAt;

    public ChangePasswordResponse(String message, Instant changedAt) {
        this.message = message;
        this.changedAt = changedAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Instant changedAt) {
        this.changedAt = changedAt;
    }
}
