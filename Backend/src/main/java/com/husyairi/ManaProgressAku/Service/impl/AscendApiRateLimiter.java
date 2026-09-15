package com.husyairi.ManaProgressAku.Service.impl;


import com.husyairi.ManaProgressAku.Entity.Model.AscendApiUsage;
import com.husyairi.ManaProgressAku.Repository.AscendApiUsageRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AscendApiRateLimiter {

    private static final Logger log = LoggerFactory.getLogger(AscendApiRateLimiter.class);

    // Monthly caps from AscendAPI: 2000/month, 1000/hour
    // we stop before hit that limit
    private static final int MONTHLY_SAFETY_THRESHOLD = 1800;
    private static final int HOURLY_SAFETY_THRESHOLD = 900;

    private final AscendApiUsageRepository ascendApiUsageRepository;

    public AscendApiRateLimiter(AscendApiUsageRepository ascendApiUsageRepository) {
        this.ascendApiUsageRepository = ascendApiUsageRepository;
    }

    /*
        Main purpose for this function is to decide whether we still can send ascend api key or not
        based on: if limit has been reached
     */
    @Transactional
    public boolean tryConsumeCall(){
        AscendApiUsage usage = ascendApiUsageRepository.findForUpdate().orElseThrow(()-> new IllegalStateException(
                "ascend_api row missing - check the migration"
        ));

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        // Reset the api count every hour and month
        resetMonthlyIfNewMonth(usage, today);
        resetHourlyIfNewHour(usage, now);

        if (usage.getMonthlyCount() >= MONTHLY_SAFETY_THRESHOLD) {
            log.warn("AscendAPI monthly safety threshold reached: {}/{}",
                    usage.getMonthlyCount(), MONTHLY_SAFETY_THRESHOLD);
            ascendApiUsageRepository.save(usage); // persist any reset that happened above
            return false;
        }

        if (usage.getHourlyCount() >= HOURLY_SAFETY_THRESHOLD) {
            log.warn("AscendAPI hourly safety threshold reached: {}/{}",
                    usage.getHourlyCount(), HOURLY_SAFETY_THRESHOLD);
            ascendApiUsageRepository.save(usage);
            return false;
        }

        // No issue, can proceed with the call
        usage.setMonthlyCount(usage.getMonthlyCount() + 1);
        usage.setHourlyCount(usage.getHourlyCount() + 1);
        ascendApiUsageRepository.save(usage);

        return true;
    }

    private void resetMonthlyIfNewMonth(AscendApiUsage usage, LocalDate today){
        LocalDate periodStart = usage.getMonthlyPeriodStart();

        // check if the current month/year is the same as last record
        boolean isNewMonth = periodStart.getMonthValue() != today.getMonthValue()
                || periodStart.getYear() != today.getYear();

        if(isNewMonth){
            log.info("AscendAPI monthly usage counter reset (was {})", usage.getMonthlyCount());
            usage.setMonthlyCount(0);
            usage.setMonthlyPeriodStart(today.withDayOfMonth(1));
        }
    }

    private void resetHourlyIfNewHour(AscendApiUsage usage, LocalDateTime now) {
        LocalDateTime periodStart = usage.getHourlyPeriodStart();
        boolean isNewHour = periodStart.getHour() != now.getHour()
                || periodStart.toLocalDate().isBefore(now.toLocalDate());

        if (isNewHour) {
            usage.setHourlyCount(0);
            usage.setHourlyPeriodStart(now.withMinute(0).withSecond(0).withNano(0));
        }
    }

}
