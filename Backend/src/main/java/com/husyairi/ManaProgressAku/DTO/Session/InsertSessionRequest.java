package com.husyairi.ManaProgressAku.DTO.Session;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

public class InsertSessionRequest {

    // we dont specify sessionID because we will generate it in the service
    @Schema(example = "2028-10-02")
    private LocalTime time;

    @Schema(example = "17:41:09")
    private LocalDate date;

    private String status;

    public InsertSessionRequest() {
    }

    public InsertSessionRequest(LocalTime time, LocalDate date, String status) {
        this.time = time;
        this.date = date;
        this.status = status;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
