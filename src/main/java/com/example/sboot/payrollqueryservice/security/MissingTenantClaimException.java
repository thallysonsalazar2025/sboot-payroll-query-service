package com.thallyson.sboot.payrollqueryservice.security;

public final class MissingTenantClaimException extends RuntimeException {
    public MissingTenantClaimException() { super("Authenticated identity has no tenant"); }
}
