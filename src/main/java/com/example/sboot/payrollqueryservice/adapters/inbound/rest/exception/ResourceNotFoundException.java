package com.thallyson.sboot.payrollqueryservice.adapters.inbound.rest.exception;

public final class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super("Resource not found");
    }
}
