package com.husyairi.ManaProgressAku;

import static org.assertj.core.api.Assertions.assertThat;

import com.husyairi.ManaProgressAku.support.IntegrationTestBase;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SchemaIT extends IntegrationTestBase {

    // Tables whose rows belong to a user, a session or an activity.
    private static final List<String> OWNED_TABLES =
            List.of("user_profile", "session", "activity", "activityset");

    private static final String FOREIGN_KEYS = """
            SELECT TABLE_NAME AS child, REFERENCED_TABLE_NAME AS parent, DELETE_RULE AS delete_rule
            FROM information_schema.REFERENTIAL_CONSTRAINTS
            WHERE CONSTRAINT_SCHEMA = DATABASE()
            """;

    @Test
    void flywayRanTheMigrationsSuccessfully() {
        List<String> versions = jdbc.queryForList(
                "SELECT version FROM flyway_schema_history WHERE success = 1 ORDER BY installed_rank",
                String.class);

        assertThat(versions).contains("1", "2");
    }

    @Test
    void theQuotaCounterTableHoldsExactlyOneRow() {
        Integer rows = jdbc.queryForObject("SELECT COUNT(*) FROM ascend_api_usage", Integer.class);

        assertThat(rows).isEqualTo(1);
    }

    // Guards the next test: if a table name is mistyped it would pass without checking anything.
    @Test
    void everyOwnedTableHasAtLeastOneForeignKey() {
        List<Object> tablesWithAKey = jdbc.queryForList(FOREIGN_KEYS).stream()
                .map(key -> key.get("child"))
                .toList();

        assertThat(tablesWithAKey).containsAll(OWNED_TABLES);
    }

    @Test
    void foreignKeysOfOwnedTablesCascadeOnDelete() {
        List<String> notCascading = jdbc.queryForList(FOREIGN_KEYS).stream()
                .filter(key -> OWNED_TABLES.contains(key.get("child")))
                .filter(key -> !"CASCADE".equals(key.get("delete_rule")))
                .map(key -> key.get("child") + " -> " + key.get("parent") + " is " + key.get("delete_rule"))
                .toList();

        assertThat(notCascading).as("foreign keys that do not cascade").isEmpty();
    }

    @Test
    void deletingAnExerciseNeverCascadesIntoWorkouts() {
        List<String> cascading = jdbc.queryForList(FOREIGN_KEYS).stream()
                .filter(key -> "exercise".equals(key.get("parent")))
                .filter(key -> "CASCADE".equals(key.get("delete_rule")))
                .map(key -> key.get("child") + " cascades from exercise")
                .toList();

        assertThat(cascading).isEmpty();
    }
}