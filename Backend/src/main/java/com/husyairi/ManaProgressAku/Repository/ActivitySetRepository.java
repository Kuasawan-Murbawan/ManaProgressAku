package com.husyairi.ManaProgressAku.Repository;

import com.husyairi.ManaProgressAku.Entity.Model.ActivitySet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivitySetRepository extends JpaRepository<ActivitySet, Long> {
    List<ActivitySet> findByActivityActivityID(String activityID);
}
