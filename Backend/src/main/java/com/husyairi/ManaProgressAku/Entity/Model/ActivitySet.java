package com.husyairi.ManaProgressAku.Entity.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "activityset")
public class ActivitySet{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "setid")
    private Long setID;

    @Column(columnDefinition = "DECIMAL(6,2)", nullable = false)
    private BigDecimal weight;

    @Column(columnDefinition = "INT", nullable = false)
    private Integer reps;

    @Column(columnDefinition = "INT", nullable = false)
    private Integer setNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "activityid",
            referencedColumnName = "activityid",
            nullable = false
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Activity activity;


    public ActivitySet(Long setID, BigDecimal weight, Integer reps, Integer setNumber, Activity activity) {
        this.setID = setID;
        this.weight = weight;
        this.reps = reps;
        this.setNumber = setNumber;
        this.activity = activity;
    }

    public ActivitySet(BigDecimal weight, Integer reps, Integer setNumber, Activity activity) {
        this.weight = weight;
        this.reps = reps;
        this.setNumber = setNumber;
        this.activity = activity;
    }

    public ActivitySet() {
    }

    public Long getSetID() {
        return setID;
    }

    public void setSetID(Long setID) {
        this.setID = setID;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Integer getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(Integer setNumber) {
        this.setNumber = setNumber;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
