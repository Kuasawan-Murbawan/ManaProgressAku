package com.husyairi.ManaProgressAku.Repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import com.husyairi.ManaProgressAku.Entity.Model.User;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository  extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
