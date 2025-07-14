package com.husyairi.ManaProgressAku.Repository;

import com.husyairi.ManaProgressAku.Entity.Model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, String> {

    List<Activity> findBySessionID(String sessionID);
    Activity findTopByOrderByActivityIDDesc();
}
