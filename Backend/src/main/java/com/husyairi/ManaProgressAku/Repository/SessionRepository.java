package com.husyairi.ManaProgressAku.Repository;

import com.husyairi.ManaProgressAku.Entity.Model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {

    /*
    findTopBy - Get only the first record
    OrderBySessionIDDesc - Order by sessionID descending (biggest one first)
     */
    Session findTopByOrderBySessionIDDesc();

    List<Session> findByUserId(Long userId);
}
