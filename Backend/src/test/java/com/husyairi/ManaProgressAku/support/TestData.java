package com.husyairi.ManaProgressAku.support;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/** Inserts minimal rows with plain SQL, so tests can set up data without entities or repositories. */
public class TestData {

    private static final String DUMMY = "x";
    private static final String DUMMY_PASSWORD = "HASH";

    // users, session and activity have no AUTO_INCREMENT, so their ids come from this counter.
    // It starts high to stay clear of the ids Hibernate hands out from users_seq.
    private static final AtomicLong NEXT_ID = new AtomicLong(1_000_000L);

    private final JdbcTemplate jdbc;

    public TestData(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public long insertUser(String email) {
        long id = NEXT_ID.incrementAndGet();
        jdbc.update(
                "INSERT INTO users (id, email, name, password, role) VALUES (?, ?, ?, ?, 'ADMIN')",
                id, email, DUMMY, DUMMY_PASSWORD);
        return id;
    }

    // The primary key of user_profile is the user id itself.
    public long insertProfile(long userId) {
        jdbc.update("INSERT INTO user_profile (user_id) VALUES (?)", userId);
        return userId;
    }

    // sessionid is a varchar, so the numeric id is stored as a string.
    public long insertSession(long userId) {
        long id = NEXT_ID.incrementAndGet();
        jdbc.update(
                "INSERT INTO `session` (sessionid, `date`, `time`, user_id, status) "
                        + "VALUES (?, CURDATE(), CURTIME(6), ?, ?)",
                String.valueOf(id), userId, DUMMY);
        return id;
    }

    // activityid and sessionid are varchars, so the numeric ids are stored as strings.
    public long insertActivity(long sessionId, long exerciseId) {
        long id = NEXT_ID.incrementAndGet();
        jdbc.update(
                "INSERT INTO activity (activityid, exerciseid, sessionid) VALUES (?, ?, ?)",
                String.valueOf(id), exerciseId, String.valueOf(sessionId));
        return id;
    }

    public long insertSet(long activityId) {
        return insertReturningKey(
                "INSERT INTO activityset (reps, set_number, weight, activityid) VALUES (1, 1, 1, ?)",
                String.valueOf(activityId));
    }

    public long insertExercise(String name) {
        return insertReturningKey(
                "INSERT INTO exercise (exercise_name, is_bodyweight) VALUES (?, false)",
                name);
    }

    public void deleteRow(String table, long id) {
        String keyColumn = jdbc.queryForObject(
                "SELECT COLUMN_NAME FROM information_schema.KEY_COLUMN_USAGE "
                        + "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND CONSTRAINT_NAME = 'PRIMARY'",
                String.class, table);

        // Bound as a string so it matches both the bigint keys and the varchar keys.
        jdbc.update("DELETE FROM `" + table + "` WHERE `" + keyColumn + "` = ?", String.valueOf(id));
    }

    private long insertReturningKey(String sql, String value) {
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, value);
            return statement;
        }, keys);
        return Objects.requireNonNull(keys.getKey()).longValue();
    }
}
