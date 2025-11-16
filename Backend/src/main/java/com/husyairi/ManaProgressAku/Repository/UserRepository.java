package com.husyairi.ManaProgressAku.Repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.husyairi.ManaProgressAku.Entity.Model.User;

import java.util.Optional;

// We use Crud repo bc we only need simple crud operations

@Repository
public interface UserRepository  extends CrudRepository<User, Long> {

    /*
    Find user using email

    SELECT *
    FROM user
    WHERE email = {email}
     */
    Optional<User> findByEmail(String email);
}
