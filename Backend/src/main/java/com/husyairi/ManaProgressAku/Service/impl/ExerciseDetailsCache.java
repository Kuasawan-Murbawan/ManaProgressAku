package com.husyairi.ManaProgressAku.Service.impl;

import com.husyairi.ManaProgressAku.DTO.Exercise.External.AscendExerciseDTO;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Service
public class ExerciseDetailsCache {


    private static final Duration TTL = Duration.ofMinutes(55);

    private final Clock clock;

    // Spring uses this one: with several constructors and no @Autowired, it picks the no-argument one
    public ExerciseDetailsCache() {
        this(Clock.systemUTC());
    }

    // Tests use this one so they can control time
    ExerciseDetailsCache(Clock clock) {
        this.clock = clock;
    }

    /*
        Store the recently fetched exercises
     */
    private class CacheEntry {
        final Optional<AscendExerciseDTO> value;
        final Instant fetchedAt;

        CacheEntry(Optional<AscendExerciseDTO> value) {
            this.value = value;
            this.fetchedAt = clock.instant();
        }

        // Make sure the cache exercise doesn't stay more than 1 hour
        boolean isExpired() {
            return clock.instant().isAfter(fetchedAt.plus(TTL));
        }

    }

    // ConcurrentHashMap will make sure if there is .put and .get request at the same time,
    // the data will not be lost or corrupted
    private final Map<String, CacheEntry> cacheMap = new ConcurrentHashMap<>();
    private final Map<String, Object> locks = new ConcurrentHashMap<>();

        public Optional<AscendExerciseDTO> getOrFetch(String ascendExerciseId, Supplier<Optional<AscendExerciseDTO>> fetcher){
            CacheEntry existingCache = cacheMap.get(ascendExerciseId);

            // The exercise exists in the cache (recently fetched from db)
            if(existingCache != null && !existingCache.isExpired()){
                return existingCache.value;
            }

            // add value to map only if the keys are absent
            Object lock = locks.computeIfAbsent(ascendExerciseId, k -> new Object());

            /*
                    SYNCHRONIZED BLOCK

                    If there are two request of the same exercise, second req will wait for the first one to finish
                    we specifically lock this object, not the whole locks map

             */
            synchronized (lock){
                // We recheck because what if when making duplicate checks, the other had finished fetching
                CacheEntry recheck = cacheMap.get(ascendExerciseId);
                if(recheck != null && !recheck.isExpired()){
                    return recheck.value;
                }

                // we will send he function to call API in the parameter of this method
                // so when fetcher.get run, it will call the api
                Optional<AscendExerciseDTO> result = fetcher.get();
                cacheMap.put(ascendExerciseId, new CacheEntry(result));
                return result;
            }

        }

}
