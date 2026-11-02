package com.husyairi.ManaProgressAku.Service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.husyairi.ManaProgressAku.Entity.Model.AscendApiUsage;
import com.husyairi.ManaProgressAku.Repository.AscendApiUsageRepository;
import com.husyairi.ManaProgressAku.support.IntegrationTestBase;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

class AscendApiRateLimiterIT extends IntegrationTestBase {

    @Autowired
    private AscendApiRateLimiter limiter;

    @Autowired
    private AscendApiUsageRepository repository;

    @Autowired
    private TransactionTemplate transaction;

    @BeforeEach
    void avoidTheTurnOfAnHour() throws InterruptedException {
        // The limiter reads the real clock, so never start a race in the last seconds of an hour.
        LocalDateTime now = LocalDateTime.now();
        if (now.getMinute() == 59 && now.getSecond() >= 55) {
            Thread.sleep(6_000);
        }
    }

    private void startWith(int monthlyCount, int hourlyCount) {
        transaction.executeWithoutResult(status -> {
            AscendApiUsage row = repository.findForUpdate().orElseThrow();
            row.setMonthlyCount(monthlyCount);
            row.setHourlyCount(hourlyCount);
            row.setMonthlyPeriodStart(LocalDate.now().withDayOfMonth(1));
            row.setHourlyPeriodStart(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS));
            repository.save(row);
        });
    }

    private AscendApiUsage rowNow() {
        return transaction.execute(status -> repository.findForUpdate().orElseThrow());
    }

    /** Starts all threads at the same moment and returns what each call answered. */
    private List<Boolean> race(int threads) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        try {
            CountDownLatch ready = new CountDownLatch(threads);
            CountDownLatch go = new CountDownLatch(1);
            List<Future<Boolean>> calls = new ArrayList<>();
            for (int i = 0; i < threads; i++) {
                calls.add(pool.submit(() -> {
                    ready.countDown();
                    go.await();
                    return limiter.tryConsumeCall();
                }));
            }
            ready.await();
            go.countDown();
            List<Boolean> answers = new ArrayList<>();
            for (Future<Boolean> call : calls) {
                answers.add(call.get(30, TimeUnit.SECONDS));
            }
            return answers;
        } finally {
            pool.shutdownNow();
        }
    }

    private static long allowed(List<Boolean> answers) {
        return answers.stream().filter(answer -> answer).count();
    }

    @Test
    void manyCallsAtOnceAreAllCountedExactlyOnce() throws Exception {
        startWith(0, 0);

        List<Boolean> answers = race(20);

        assertThat(answers).containsOnly(true);
        AscendApiUsage row = rowNow();
        assertThat(row.getHourlyCount()).isEqualTo(20);
        assertThat(row.getMonthlyCount()).isEqualTo(20);
    }

    @Test
    void onlyTheCallsLeftInTheHourAreAllowed() throws Exception {
        startWith(0, 890);

        List<Boolean> answers = race(30);

        assertThat(allowed(answers)).isEqualTo(10);
        AscendApiUsage row = rowNow();
        assertThat(row.getHourlyCount()).isEqualTo(900);
        assertThat(row.getMonthlyCount()).isEqualTo(10);
    }

    @Test
    void onlyTheCallsLeftInTheMonthAreAllowed() throws Exception {
        startWith(1795, 0);

        List<Boolean> answers = race(12);

        assertThat(allowed(answers)).isEqualTo(5);
        assertThat(rowNow().getMonthlyCount()).isEqualTo(1800);
    }
}