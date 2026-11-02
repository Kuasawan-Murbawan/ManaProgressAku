package com.husyairi.ManaProgressAku.support;

import java.sql.Statement;
import java.util.List;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

/** Empties every table except the migration history and the seeded quota row. */
public final class DatabaseCleaner {

    private static final List<String> KEEP = List.of("flyway_schema_history", "ascend_api_usage");

    private DatabaseCleaner() {
    }

    public static void clean(JdbcTemplate jdbc) {
        List<String> tables = jdbc.queryForList(
                "SELECT TABLE_NAME FROM information_schema.TABLES "
                        + "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_TYPE = 'BASE TABLE'",
                String.class);

        // SET FOREIGN_KEY_CHECKS only lasts for one connection, so everything runs on the same one.
        jdbc.execute((ConnectionCallback<Void>) connection -> {
            try (Statement statement = connection.createStatement()) {
                statement.execute("SET FOREIGN_KEY_CHECKS = 0");
                try {
                    for (String table : tables) {
                        if (!KEEP.contains(table.toLowerCase())) {
                            statement.execute("TRUNCATE TABLE `" + table + "`");
                        }
                    }
                } finally {
                    statement.execute("SET FOREIGN_KEY_CHECKS = 1");
                }
            }
            return null;
        });
    }
}