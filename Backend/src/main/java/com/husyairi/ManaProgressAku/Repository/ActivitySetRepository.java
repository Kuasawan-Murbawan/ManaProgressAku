package com.husyairi.ManaProgressAku.Repository;

import com.husyairi.ManaProgressAku.Entity.Model.ActivitySet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivitySetRepository extends JpaRepository<ActivitySet, Long> {
//    List<ActivitySet> findByActivityActivityID(String activityID);

    @Query(value = "SELECT * FROM activityset WHERE activityid= :activityID", nativeQuery = true)
    List<ActivitySet> findSetsByActivityID(String activityID);

}
