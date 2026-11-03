package com.husyairi.ManaProgressAku.ExceptionHandling;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @ParameterizedTest(name = "error id {0} becomes HTTP {1}")
    @CsvSource({"400,400", "401,401", "403,403", "404,404", "409,409", "429,429", "500,500", "502,502"})
    void badRequestExceptionMapsErrorIdToHttpStatus(int errorId, int expectedHttpStatus){

        // From catched exception, maps it into respective error id
        BadRequestException exception = new BadRequestException(errorId, "some message", Map.of("field", "detail"));

        ResponseEntity<ApiErrorResponse> response = handler.handleBadRequestException(exception);

        assertThat(response.getStatusCode().value()).isEqualTo(expectedHttpStatus);
        ApiErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getErrorCode()).isEqualTo(errorId);
        assertThat(body.getErrorMessage()).isEqualTo("some message");
        assertThat(body.getErrorDetails()).containsEntry("field", "detail");
    }

    @Test
    void unknownErrorIdFallsbackToBadRequest(){
        BadRequestException exception = new BadRequestException(418, "teapot", Map.of());

        ResponseEntity<ApiErrorResponse> response = handler.handleBadRequestException(exception);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }


    @Test
    void validationFailureListsEveryFieldError() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("updateProfileRequest", "weightKg", "Weight must be greater than 0"),
                new FieldError("updateProfileRequest", "heightCm", "Height seems unrealistic")));
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ApiErrorResponse> response = handler.handleValidationException(exception);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        ApiErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getErrorMessage()).isEqualTo("Validation failed");
        assertThat(body.getErrorDetails())
                .containsEntry("weightKg", "Weight must be greater than 0")
                .containsEntry("heightCm", "Height seems unrealistic");
    }

    @Test
    void malformedBodyGives400WithEmptyDetails() {
        ResponseEntity<ApiErrorResponse> response =
                handler.handleUnreadableBody(mock(HttpMessageNotReadableException.class));

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        ApiErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getErrorMessage()).isEqualTo("Malformed request body");
        assertThat(body.getErrorDetails()).isEmpty();
    }

    @Test
    void accessDeniedGives403NotTheDefault401() {
        ResponseEntity<ApiErrorResponse> response =
                handler.handleAccessDenied(new AccessDeniedException("denied"));

        assertThat(response.getStatusCode().value()).isEqualTo(403);
        ApiErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getErrorCode()).isEqualTo(403);
        assertThat(body.getErrorMessage()).isEqualTo("You do not have permission to perform this action");
    }


}
