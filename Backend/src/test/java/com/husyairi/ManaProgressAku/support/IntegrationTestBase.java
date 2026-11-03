package com.husyairi.ManaProgressAku.support;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

// Every database test extends this class, so they all share one Spring context and one MySQL container.
@SpringBootTest
@ActiveProfiles("test")
@Import(MySqlContainerConfig.class)
public abstract class IntegrationTestBase {

    @Autowired
    protected JdbcTemplate jdbc;

    @BeforeEach
    void cleanTheDatabase() {
        DatabaseCleaner.clean(jdbc);
    }
}