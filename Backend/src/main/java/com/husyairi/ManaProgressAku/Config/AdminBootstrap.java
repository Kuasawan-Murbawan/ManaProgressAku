package com.husyairi.ManaProgressAku.Config;

import com.husyairi.ManaProgressAku.Entity.Model.User;
import com.husyairi.ManaProgressAku.Enums.Role;
import com.husyairi.ManaProgressAku.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Creates the admin account at start-up from ADMIN_EMAIL and ADMIN_PW, if both are set
// and no user has that email yet. Never logs either value.
@Component
public class AdminBootstrap implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminBootstrap.class);

    static final String ADMIN_NAME = "Admin";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    public AdminBootstrap(UserRepository userRepository, PasswordEncoder passwordEncoder, Environment environment) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
    }

    @Override
    public void run(String... args) {
        // getProperty returns null when unset: no defaults, and an unset value never stops start-up.
        String email = environment.getProperty("ADMIN_EMAIL");
        String password = environment.getProperty("ADMIN_PW");

        if (isBlank(email) || isBlank(password) || userRepository.findByEmail(email).isPresent()) {
            log.info("Admin bootstrap: skipped");
            return;
        }

        // id stays null so the users_seq generator assigns it, as with any other saved user.
        userRepository.save(new User(ADMIN_NAME, email, passwordEncoder.encode(password), Role.ADMIN));
        log.info("Admin bootstrap: created");
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
