package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.Exercise.External.AscendExerciseDTO;
import com.husyairi.ManaProgressAku.support.MutableClock;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ExerciseDetailsCacheTest {

    private final MutableClock clock = new MutableClock(Instant.parse("2026-10-27T10:00:00Z"), ZoneOffset.UTC);
    private final ExerciseDetailsCache cache = new ExerciseDetailsCache(clock);
    private final AscendExerciseDTO dto = mock(AscendExerciseDTO.class);
    private final AtomicInteger fetchCount = new AtomicInteger();

    private Supplier<Optional<AscendExerciseDTO>> fetcherReturning(Optional<AscendExerciseDTO> result) {
        return () -> {
            fetchCount.incrementAndGet();
            return result;
        };
    }

    private static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    void firstCallRunsTheFetcherAndReturnsItsResult() {
        Optional<AscendExerciseDTO> result = cache.getOrFetch("0001", fetcherReturning(Optional.of(dto)));

        assertThat(result).containsSame(dto);
        assertThat(fetchCount).hasValue(1);
    }

    @Test
    void secondCallInsideTheTtlComesFromTheCache() {
        cache.getOrFetch("0001", fetcherReturning(Optional.of(dto)));
        clock.advance(Duration.ofMinutes(54));

        Optional<AscendExerciseDTO> result = cache.getOrFetch("0001", fetcherReturning(Optional.of(dto)));

        assertThat(result).containsSame(dto);
        assertThat(fetchCount).hasValue(1);
    }

    @Test
    void entryIsStillValidAtExactly55MinutesAndExpiresJustAfter() {
        cache.getOrFetch("0001", fetcherReturning(Optional.of(dto)));

        clock.advance(Duration.ofMinutes(55));
        cache.getOrFetch("0001", fetcherReturning(Optional.of(dto)));
        assertThat(fetchCount).as("exactly at the limit").hasValue(1);

        clock.advance(Duration.ofSeconds(1));
        cache.getOrFetch("0001", fetcherReturning(Optional.of(dto)));
        assertThat(fetchCount).as("one second past the limit").hasValue(2);
    }

    @Test
    void aNotFoundResultIsCachedToo() {
        Optional<AscendExerciseDTO> first = cache.getOrFetch("missing", fetcherReturning(Optional.empty()));
        Optional<AscendExerciseDTO> second = cache.getOrFetch("missing", fetcherReturning(Optional.empty()));

        assertThat(first).isEmpty();
        assertThat(second).isEmpty();
        assertThat(fetchCount).hasValue(1);
    }

    @Test
    void differentIdsAreCachedSeparately() {
        cache.getOrFetch("0001", fetcherReturning(Optional.of(dto)));
        cache.getOrFetch("0002", fetcherReturning(Optional.of(dto)));

        assertThat(fetchCount).hasValue(2);
    }

    @Test
    void aFailedFetchIsNotCachedSoTheNextCallTriesAgain() {
        assertThatThrownBy(() -> cache.getOrFetch("0001", () -> {
            throw new IllegalStateException("rate limited");
        })).isInstanceOf(IllegalStateException.class);

        Optional<AscendExerciseDTO> result = cache.getOrFetch("0001", fetcherReturning(Optional.of(dto)));

        assertThat(result).containsSame(dto);
        assertThat(fetchCount).hasValue(1);
    }

    @Test
    void manyCallersAskingAtTheSameTimeCauseOnlyOneFetch() throws Exception {
        int callers = 8;
        CountDownLatch startTogether = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(callers);
        try {
            List<Future<Optional<AscendExerciseDTO>>> results = new ArrayList<>();
            for (int i = 0; i < callers; i++) {
                results.add(pool.submit(() -> {
                    startTogether.await();
                    return cache.getOrFetch("0001", () -> {
                        fetchCount.incrementAndGet();
                        pause(150);
                        return Optional.of(dto);
                    });
                }));
            }
            startTogether.countDown();

            for (Future<Optional<AscendExerciseDTO>> result : results) {
                assertThat(result.get(5, TimeUnit.SECONDS)).containsSame(dto);
            }
        } finally {
            pool.shutdownNow();
        }

        assertThat(fetchCount).hasValue(1);
    }
}