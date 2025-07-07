package com.husyairi.ManaProgressAku.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class InsertSessionRequest {

    // we dont specify sessionID because we will generate it in the service

    private LocalTime time;

    private LocalDate date;

    public InsertSessionRequest() {
    }

    public InsertSessionRequest(LocalTime time, LocalDate date) {
        this.time = time;
        this.date = date;
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

}
