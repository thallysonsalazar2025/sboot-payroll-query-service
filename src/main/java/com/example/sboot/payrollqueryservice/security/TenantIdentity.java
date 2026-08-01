package com.thallyson.sboot.payrollqueryservice.security;

import org.springframework.security.oauth2.jwt.Jwt;

public final class TenantIdentity {
    private TenantIdentity() {}

    public static String companyId(Jwt jwt) {
        String companyId = jwt.getClaimAsString("companyId");
        if (companyId == null || companyId.isBlank()) {
            throw new MissingTenantClaimException();
        }
        return companyId;
    }

    public static boolean canReadEmployee(Jwt jwt, String employeeId) {
        var roles = jwt.getClaimAsStringList("roles");
        return (roles != null && roles.contains("ROLE_ADMIN"))
                || (employeeId != null && employeeId.equals(jwt.getClaimAsString("employeeId")));
    }
}
