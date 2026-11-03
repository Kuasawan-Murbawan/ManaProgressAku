package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.Entity.Model.AscendApiUsage;
import com.husyairi.ManaProgressAku.Repository.AscendApiUsageRepository;
import com.husyairi.ManaProgressAku.support.MutableClock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AscendApiRateLimiterTest {

    // The fake current time is 27 Oct 2026, 10:30
    private static final Instant NOW = Instant.parse("2026-10-27T10:30:00Z");
    private static final LocalDate THIS_MONTH_START = LocalDate.of(2026, 10, 1);
    private static final LocalDateTime THIS_HOUR_START = LocalDateTime.of(2026, 10, 27, 10, 0);

    @Mock
    private AscendApiUsageRepository repository;

    private AscendApiRateLimiter limiter;

    @BeforeEach
    void setUp() {
        limiter = new AscendApiRateLimiter(repository, new MutableClock(NOW, ZoneOffset.UTC));
    }

    /** usage row that belongs to the current month and hour, already returned by the repository. */
    private AscendApiUsage usage(int monthlyCount, int hourlyCount) {
        return usage(monthlyCount, hourlyCount, THIS_MONTH_START, THIS_HOUR_START);
    }

    private AscendApiUsage usage(int monthlyCount, int hourlyCount,
                                 LocalDate monthlyPeriodStart, LocalDateTime hourlyPeriodStart) {
        AscendApiUsage row = new AscendApiUsage();
        row.setMonthlyCount(monthlyCount);
        row.setHourlyCount(hourlyCount);
        row.setMonthlyPeriodStart(monthlyPeriodStart);
        row.setHourlyPeriodStart(hourlyPeriodStart);
        when(repository.findForUpdate()).thenReturn(Optional.of(row));
        return row;
    }

    @Test
    void allowsACallUnderBothLimitsAndCountsIt() {
        AscendApiUsage row = usage(10, 5);

        assertThat(limiter.tryConsumeCall()).isTrue();

        assertThat(row.getMonthlyCount()).isEqualTo(11);
        assertThat(row.getHourlyCount()).isEqualTo(6);
        verify(repository).save(row);
    }

    @Test
    void allowsTheLastCallBeforeTheMonthlyThreshold() {
        AscendApiUsage row = usage(1799, 5);

        assertThat(limiter.tryConsumeCall()).isTrue();

        assertThat(row.getMonthlyCount()).isEqualTo(1800);
    }

    @Test
    void refusesOnceTheMonthlyThresholdIsReached() {
        AscendApiUsage row = usage(1800, 5);

        assertThat(limiter.tryConsumeCall()).isFalse();

        assertThat(row.getMonthlyCount()).isEqualTo(1800);
        assertThat(row.getHourlyCount()).isEqualTo(5);
        verify(repository).save(row);
    }

    @Test
    void allowsTheLastCallBeforeTheHourlyThreshold() {
        AscendApiUsage row = usage(10, 899);

        assertThat(limiter.tryConsumeCall()).isTrue();

        assertThat(row.getHourlyCount()).isEqualTo(900);
    }

    @Test
    void refusesOnceTheHourlyThresholdIsReached() {
        AscendApiUsage row = usage(10, 900);

        assertThat(limiter.tryConsumeCall()).isFalse();

        assertThat(row.getMonthlyCount()).isEqualTo(10);
        assertThat(row.getHourlyCount()).isEqualTo(900);
    }

    @Test
    void aNewMonthResetsTheMonthlyCounterEvenIfTheOldOneWasFull() {
        AscendApiUsage row = usage(1800, 5, LocalDate.of(2026, 9, 1), THIS_HOUR_START);

        assertThat(limiter.tryConsumeCall()).isTrue();

        assertThat(row.getMonthlyCount()).isEqualTo(1);
        assertThat(row.getMonthlyPeriodStart()).isEqualTo(THIS_MONTH_START);
    }

    @Test
    void sameMonthNumberInAnEarlierYearAlsoCountsAsANewMonth() {
        AscendApiUsage row = usage(1800, 5, LocalDate.of(2025, 10, 1), THIS_HOUR_START);

        assertThat(limiter.tryConsumeCall()).isTrue();

        assertThat(row.getMonthlyCount()).isEqualTo(1);
    }

    @Test
    void aNewHourResetsTheHourlyCounter() {
        AscendApiUsage row = usage(10, 900, THIS_MONTH_START, LocalDateTime.of(2026, 10, 27, 9, 0));

        assertThat(limiter.tryConsumeCall()).isTrue();

        assertThat(row.getHourlyCount()).isEqualTo(1);
        assertThat(row.getHourlyPeriodStart()).isEqualTo(THIS_HOUR_START);
        assertThat(row.getMonthlyCount()).isEqualTo(11);
    }

    @Test
    void sameHourOfThePreviousDayAlsoCountsAsANewHour() {
        AscendApiUsage row = usage(10, 900, THIS_MONTH_START, LocalDateTime.of(2026, 10, 26, 10, 0));

        assertThat(limiter.tryConsumeCall()).isTrue();

        assertThat(row.getHourlyCount()).isEqualTo(1);
    }

    @Test
    void aMissingUsageRowFailsLoudlyInsteadOfAllowingCalls() {
        when(repository.findForUpdate()).thenReturn(Optional.empty());

        assertThatThrownBy(() -> limiter.tryConsumeCall())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ascend_api");
    }
}