package com.husyairi.ManaProgressAku.Entity.Model;

import jakarta.persistence.*;

        import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ascend_api_usage")
public class AscendApiUsage {

    @Id
    private Integer id;

    @Column(name = "monthly_count", nullable = false)
    private Integer monthlyCount;

    @Column(name = "monthly_period_start", nullable = false)
    private LocalDate monthlyPeriodStart;

    @Column(name = "hourly_count", nullable = false)
    private Integer hourlyCount;

    @Column(name = "hourly_period_start", nullable = false)
    private LocalDateTime hourlyPeriodStart;

    public AscendApiUsage() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMonthlyCount() {
        return monthlyCount;
    }

    public void setMonthlyCount(Integer monthlyCount) {
        this.monthlyCount = monthlyCount;
    }

    public LocalDate getMonthlyPeriodStart() {
        return monthlyPeriodStart;
    }

    public void setMonthlyPeriodStart(LocalDate monthlyPeriodStart) {
        this.monthlyPeriodStart = monthlyPeriodStart;
    }

    public Integer getHourlyCount() {
        return hourlyCount;
    }

    public void setHourlyCount(Integer hourlyCount) {
        this.hourlyCount = hourlyCount;
    }

    public LocalDateTime getHourlyPeriodStart() {
        return hourlyPeriodStart;
    }

    public void setHourlyPeriodStart(LocalDateTime hourlyPeriodStart) {
        this.hourlyPeriodStart = hourlyPeriodStart;
    }
}