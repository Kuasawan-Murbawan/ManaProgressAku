package com.husyairi.ManaProgressAku.Entity.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "activity")
public class Activity {

    @Id
    @Column(name = "activityid", columnDefinition = "VARCHAR(20)", nullable = false)
    private String activityID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessionid", referencedColumnName = "sessionid", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Session session;

    @Column(nullable = false)
    private Integer exerciseID;

    @OneToMany(
            mappedBy = "activity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnoreProperties("activity")
    private List<ActivitySet> activitySets = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime created_at;

    public Activity(){}

    public Activity(String activityID, Session session, Integer exerciseID, List<ActivitySet> activitySets, LocalDateTime created_at) {
        this.activityID = activityID;
        this.session = session;
        this.exerciseID = exerciseID;
        this.activitySets = activitySets;
        this.created_at = created_at;
    }

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Integer getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(Integer exerciseID) {
        this.exerciseID = exerciseID;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
