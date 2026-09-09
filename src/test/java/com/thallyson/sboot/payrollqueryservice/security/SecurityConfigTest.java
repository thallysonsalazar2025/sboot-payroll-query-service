package com.thallyson.sboot.payrollqueryservice.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

class SecurityConfigTest {
    @Test
    void rejectsMissingAndShortHs512Secrets() {
        SecurityConfig config = new SecurityConfig();
        assertThrows(IllegalStateException.class, () -> config.jwtDecoder(null));
        assertThrows(IllegalStateException.class, () -> config.jwtDecoder("short-secret"));
    }

    @Test
    void evaluatesAdminSelfAndDeniedEmployeeAccess() {
        assertTrue(TenantIdentity.canReadEmployee(jwt("employee-1", List.of("ROLE_ADMIN")), "employee-2"));
        assertTrue(TenantIdentity.canReadEmployee(jwt("employee-1", List.of("ROLE_EMPLOYEE")), "employee-1"));
        assertFalse(TenantIdentity.canReadEmployee(jwt("employee-1", List.of("ROLE_EMPLOYEE")), null));
        assertFalse(TenantIdentity.canReadEmployee(jwt("employee-1", null), "employee-2"));
    }

    private Jwt jwt(String employeeId, List<String> roles) {
        var builder = Jwt.withTokenValue("token").header("alg", "HS512").subject("user")
                .claim("companyId", "tenant-a").claim("employeeId", employeeId);
        if (roles != null) {
            builder.claim("roles", roles);
        }
        return builder.build();
    }
}
