package com.husyairi.ManaProgressAku;

import static org.assertj.core.api.Assertions.assertThat;

import com.husyairi.ManaProgressAku.Repository.UserRepository;
import com.husyairi.ManaProgressAku.support.IntegrationTestBase;
import com.husyairi.ManaProgressAku.support.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.springframework.dao.DataIntegrityViolationException;

class CascadeDeleteIT extends IntegrationTestBase {

    @Autowired
    private UserRepository userRepository;

    private TestData data;
    private long exerciseId;

    @BeforeEach
    void setUpData() {
        data = new TestData(jdbc);
        exerciseId = data.insertExercise("Squat");
    }

    /** One user with a profile, one session, one activity and two sets. Returns the user id. */
    private long userWithAWorkout(String email) {
        long userId = data.insertUser(email);
        data.insertProfile(userId);
        long sessionId = data.insertSession(userId);
        long activityId = data.insertActivity(sessionId, exerciseId);
        data.insertSet(activityId);
        data.insertSet(activityId);
        return userId;
    }

    private int rowsIn(String table) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM `" + table + "`", Integer.class);
    }

    private void assertRowCounts(int users, int profiles, int sessions, int activities, int sets) {
        assertThat(rowsIn("users")).as("users").isEqualTo(users);
        assertThat(rowsIn("user_profile")).as("user_profile").isEqualTo(profiles);
        assertThat(rowsIn("session")).as("session").isEqualTo(sessions);
        assertThat(rowsIn("activity")).as("activity").isEqualTo(activities);
        assertThat(rowsIn("activityset")).as("activityset").isEqualTo(sets);
    }

    @Test
    void theTestDataBuildsACompleteWorkoutTree() {
        userWithAWorkout("one@test.com");

        assertRowCounts(1, 1, 1, 1, 2);
    }

    // The database rule alone, with no JPA involved.
    @Test
    void deletingAUserInTheDatabaseRemovesEverythingTheyOwn() {
        long leaving = userWithAWorkout("leaving@test.com");
        userWithAWorkout("staying@test.com");

        data.deleteRow("users", leaving);

        assertRowCounts(1, 1, 1, 1, 2);
        assertThat(rowsIn("exercise")).as("the shared exercise stays").isEqualTo(1);
    }

    // The path your app really takes. It may pass even if the database rule is missing,
    // when an entity cascades by itself, which is why the test above exists.
    @Test
    void deletingAUserThroughTheRepositoryRemovesEverythingTheyOwn() {
        long leaving = userWithAWorkout("leaving@test.com");
        userWithAWorkout("staying@test.com");

        userRepository.deleteById(leaving);

        assertRowCounts(1, 1, 1, 1, 2);
    }

    @Test
    void deletingASessionRemovesItsActivitiesAndSetsButNotTheUser() {
        long userId = data.insertUser("owner@test.com");
        long sessionId = data.insertSession(userId);
        long activityId = data.insertActivity(sessionId, exerciseId);
        data.insertSet(activityId);

        data.deleteRow("session", sessionId);

        assertRowCounts(1, 0, 0, 0, 0);
    }

    @Test
    void deletingAnActivityRemovesItsSetsButNotItsSession() {
        long userId = data.insertUser("owner@test.com");
        long sessionId = data.insertSession(userId);
        long activityId = data.insertActivity(sessionId, exerciseId);
        data.insertSet(activityId);

        data.deleteRow("activity", activityId);

        assertRowCounts(1, 0, 1, 0, 0);
    }

    @Disabled("activity.exerciseID has no foreign key. Enable after the key is added in a later release.")
    @Test
    void anExerciseThatAWorkoutUsesCannotBeDeleted() {
        userWithAWorkout("owner@test.com");

        assertThatThrownBy(() -> data.deleteRow("exercise", exerciseId))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThat(rowsIn("exercise")).isEqualTo(1);
    }
}