package com.husyairi.ManaProgressAku.support;

import com.husyairi.ManaProgressAku.ExceptionHandling.BadRequestException;
import org.assertj.core.api.ThrowableAssert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class BadRequestAssertions {

    /** Runs code that should fail and hands back the BadRequestException so a test can inspect it. */
    private BadRequestAssertions() {
    }

    public static BadRequestException thrownBadRequest(ThrowableAssert.ThrowingCallable call) {
        Throwable thrown = catchThrowable(call);
        assertThat(thrown).as("expected a BadRequestException").isInstanceOf(BadRequestException.class);
        return (BadRequestException) thrown;
    }

}
