package com.husyairi.ManaProgressAku.Config;

import com.husyairi.ManaProgressAku.Entity.Model.User;
import com.husyairi.ManaProgressAku.Enums.Role;
import com.husyairi.ManaProgressAku.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class AdminBootstrapTest {

    // Fake values that exist only in this test.
    private static final String EMAIL = "bootstrap-admin@example.test";
    private static final String PASSWORD = "Fake-Bootstrap-Pw-1!";

    @Mock
    private UserRepository userRepository;

    // The same encoder the app registers in ApplicationConfiguration.
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private AdminBootstrap bootstrapWith(MockEnvironment environment) {
        return new AdminBootstrap(userRepository, passwordEncoder, environment);
    }

    private MockEnvironment env(String email, String password) {
        MockEnvironment environment = new MockEnvironment();
        if (email != null) {
            environment.setProperty("ADMIN_EMAIL", email);
        }
        if (password != null) {
            environment.setProperty("ADMIN_PW", password);
        }
        return environment;
    }

    @Test
    void skipsWhenAdminEmailIsUnset(CapturedOutput output) {
        bootstrapWith(env(null, PASSWORD)).run();

        verifyNoInteractions(userRepository);
        assertThat(output).contains("Admin bootstrap: skipped");
    }

    @Test
    void skipsWhenAdminPasswordIsUnset(CapturedOutput output) {
        bootstrapWith(env(EMAIL, null)).run();

        verifyNoInteractions(userRepository);
        assertThat(output).contains("Admin bootstrap: skipped");
    }

    @Test
    void skipsWhenEitherValueIsBlank() {
        bootstrapWith(env("   ", PASSWORD)).run();
        bootstrapWith(env(EMAIL, "")).run();

        verifyNoInteractions(userRepository);
    }

    @Test
    void skipsWhenThatEmailAlreadyExistsAndLeavesItsPasswordAlone(CapturedOutput output) {
        User existing = new User("Someone", EMAIL, "EXISTING_HASH", Role.USER);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(existing));

        bootstrapWith(env(EMAIL, PASSWORD)).run();

        verify(userRepository, never()).save(any(User.class));
        assertThat(existing.getPassword()).isEqualTo("EXISTING_HASH");
        assertThat(output).contains("Admin bootstrap: skipped");
    }

    @Test
    void createsExactlyOneAdminWithAnEncodedPassword(CapturedOutput output) {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        bootstrapWith(env(EMAIL, PASSWORD)).run();

        ArgumentCaptor<User> saved = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(saved.capture());
        User admin = saved.getValue();

        assertThat(admin.getRole()).isEqualTo(Role.ADMIN);
        assertThat(admin.getEmail()).isEqualTo(EMAIL);
        assertThat(admin.getName()).isNotBlank();
        assertThat(admin.getPassword()).isNotEqualTo(PASSWORD);
        assertThat(passwordEncoder.matches(PASSWORD, admin.getPassword())).isTrue();
        // Left null so the users_seq generator assigns it on save.
        assertThat(admin.getId()).isNull();
        assertThat(output).contains("Admin bootstrap: created");
    }

    @Test
    void logOutputNeverContainsTheEmailOrPassword(CapturedOutput output) {
        when(userRepository.findByEmail(EMAIL))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(new User(AdminBootstrap.ADMIN_NAME, EMAIL, "HASH", Role.ADMIN)));

        bootstrapWith(env(EMAIL, PASSWORD)).run();   // created
        bootstrapWith(env(EMAIL, PASSWORD)).run();   // skipped, already exists
        bootstrapWith(env(EMAIL, null)).run();       // skipped, password unset

        assertThat(output).contains("Admin bootstrap: created", "Admin bootstrap: skipped");
        assertThat(output).doesNotContain(EMAIL);
        assertThat(output).doesNotContain(PASSWORD);
    }
}
