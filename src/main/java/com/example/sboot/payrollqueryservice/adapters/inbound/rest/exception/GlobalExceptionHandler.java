package com.thallyson.sboot.payrollqueryservice.adapters.inbound.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import com.thallyson.sboot.payrollqueryservice.security.MissingTenantClaimException;
import org.springframework.security.access.AccessDeniedException;
import java.util.Map;
import java.time.DateTimeException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity<>(Map.of("error", "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(RuntimeException ex) {
        return new ResponseEntity<>(Map.of("error", "Resource not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AccessDeniedException.class, MissingTenantClaimException.class})
    public ResponseEntity<Map<String, String>> handleForbidden(RuntimeException ex) {
        return new ResponseEntity<>(Map.of("error", "Access denied"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        return new ResponseEntity<>(Map.of("error", "Invalid request"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<Map<String, String>> handleInvalidDate(DateTimeException ex) {
        return new ResponseEntity<>(Map.of("error", "Invalid request"), HttpStatus.BAD_REQUEST);
    }
}
