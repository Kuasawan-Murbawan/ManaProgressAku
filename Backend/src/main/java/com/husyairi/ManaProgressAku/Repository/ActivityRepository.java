package com.husyairi.ManaProgressAku.Repository;

import com.husyairi.ManaProgressAku.Entity.Model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, String> {

    /*
    Find all activities that have this session id

    SELECT *
    FROM activity
    JOIN session ON activity.session_id = session.session_id
    WHERE session.session_id = {sessionID}
     */
    List<Activity> findBySession_SessionID(String sessionID);

    /*
    Find the latest activityID

    SELECT *
    FROM activity
    ORDER BY activity.activity_id DESC
    LIMIT 1
     */
    Activity findTopByOrderByActivityIDDesc();
}
