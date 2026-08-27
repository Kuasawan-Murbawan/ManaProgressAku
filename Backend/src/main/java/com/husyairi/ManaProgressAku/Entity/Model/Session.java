package com.husyairi.ManaProgressAku.Entity.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private User user;


    // ACTIVE or COMPLETED
    @Column(name = "status", nullable = false)
    private String status;

    public Session() {
    }

    public Session(String sessionID, LocalDate date, LocalTime time) {
        this.sessionID = sessionID;
        this.date = date;
        this.time = time;
    }

    public Session(LocalDate date, LocalTime time, String status) {
        this.date = date;
        this.time = time;
        this.status = status;
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

    public Long getUser() {
        return user != null ? user.getId() : null;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Convenience getter for existing call sites that only ever wanted the raw ID
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
