package com.thallyson.sboot.payrollqueryservice;

import static org.assertj.core.api.Assertions.assertThat;

import com.thallyson.sboot.payrollqueryservice.adapters.inbound.rest.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class GlobalExceptionHandlerTest {
    @Test
    void sanitizesUnexpectedFailureWithoutLeakingSensitiveMessage() {
        String sensitiveDetail = "SQL error: salary=10000.10 token=secret";

        var response = new GlobalExceptionHandler().handleAllExceptions(
                new RuntimeException(sensitiveDetail), null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).containsEntry("error", "Internal server error");
        assertThat(response.getBody().toString()).doesNotContain(sensitiveDetail, "salary", "token", "SQL");
    }

    @Test
    void sanitizesInvalidArguments() {
        var response = new GlobalExceptionHandler().handleBadRequest(
                new IllegalArgumentException("salary and SQL details"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("error", "Invalid request");
        assertThat(response.getBody().toString()).doesNotContain("salary", "SQL");
    }
}
