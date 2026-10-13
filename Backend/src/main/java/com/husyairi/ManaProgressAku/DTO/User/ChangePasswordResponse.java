package com.husyairi.ManaProgressAku.DTO.User;

import java.time.LocalDateTime;

public class ChangePasswordResponse {
    private boolean isSuccessful;
    private LocalDateTime changedAt;

    public ChangePasswordResponse(boolean isSuccessful, LocalDateTime changedAt) {
        this.isSuccessful = isSuccessful;
        this.changedAt = changedAt;
    }

    public boolean getSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(Boolean successful) {
        isSuccessful = successful;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }
}
