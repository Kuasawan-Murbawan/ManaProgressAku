package com.husyairi.ManaProgressAku.Entity.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalTime;

// TODO: can we generate ID consist of string and integer

@Entity
@Table(name = "session")
public class Session {

    @Id
    @Column(columnDefinition = "VARCHAR(20)")
    private String sessionID;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private LocalDate date;

    public Session() {
    }

    public Session(String sessionID, LocalDate date, LocalTime time) {
        this.sessionID = sessionID;
        this.date = date;
        this.time = time;
    }

    public Session(LocalDate date, LocalTime time) {
        this.date = date;
        this.time = time;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
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
