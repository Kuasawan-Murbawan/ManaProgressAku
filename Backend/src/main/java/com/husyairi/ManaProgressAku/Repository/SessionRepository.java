package com.husyairi.ManaProgressAku.Repository;

import com.husyairi.ManaProgressAku.Entity.Model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
