package com.husyairi.ManaProgressAku.Repository;

import com.husyairi.ManaProgressAku.Entity.Model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {

    /*
    Find the highest/latest sessionID value
    findTopBy -> Get only the first record
    OrderBySessionIDDesc -> Order by sessionID descending (biggest one first)

    SELECT *
    FROM session
    ORDER BY session_id DESC
    LIMIT 1
     */
    Session findTopByOrderBySessionIDDesc();

    /*
    Find Sessions based on userId

    SELECT *
    FROM session
    WHERE user_id = {userId}
     */
    List<Session> findByUserId(Long userId);

    @Query(value = "SELECT * FROM session WHERE user_id= :userID AND status='ACTIVE'", nativeQuery = true)
    Optional<Session> findActiveSession(Long userID);
}
