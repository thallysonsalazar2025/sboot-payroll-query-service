package com.thallyson.sboot.payrollqueryservice;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.thallyson.sboot.payrollqueryservice.adapters.outbound.persistence.jpa.PayrollJpaEntity;
import com.thallyson.sboot.payrollqueryservice.adapters.outbound.persistence.jpa.SpringDataPayrollRepository;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "security.jwt.secret=test-secret-for-hs512-must-have-at-least-sixty-four-characters-123456789",
        "spring.datasource.url=jdbc:h2:mem:payroll;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password="
})
@AutoConfigureMockMvc
class PayrollTenantIsolationIntegrationTest {
    private static final String JWT_SECRET =
            "test-secret-for-hs512-must-have-at-least-sixty-four-characters-123456789";
    @Autowired MockMvc mvc;
    @Autowired SpringDataPayrollRepository repository;

    @BeforeEach
    void seed() {
        repository.deleteAll();
        repository.save(new PayrollJpaEntity(null, "tenant-a", "employee-1", LocalDate.of(2026, 7, 1),
                new BigDecimal("10000.10"), new BigDecimal("2000.05"), new BigDecimal("8000.05")));
        repository.save(new PayrollJpaEntity(null, "tenant-b", "employee-1", LocalDate.of(2026, 7, 1),
                new BigDecimal("7000.10"), new BigDecimal("1000.05"), new BigDecimal("6000.05")));
    }

    @Test
    void ownTenantCanReadPayrollAndPreservesMoney() throws Exception {
        mvc.perform(get("/api/payroll/employee-1").param("year", "2026").param("month", "7")
                        .with(identity("tenant-a", "employee-1", "ROLE_EMPLOYEE")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.grossSalary").value(10000.10))
                .andExpect(jsonPath("$.netSalary").value(8000.05));
    }

    @Test
    void sameEmployeeAndCompetenceRemainIsolatedInBothDirections() throws Exception {
        mvc.perform(get("/api/payroll/employee-1").param("year", "2026").param("month", "7")
                        .with(identity("tenant-a", "employee-1", "ROLE_EMPLOYEE")))
                .andExpect(jsonPath("$.netSalary").value(8000.05));
        mvc.perform(get("/api/payroll/employee-1").param("year", "2026").param("month", "7")
                        .with(identity("tenant-b", "employee-1", "ROLE_EMPLOYEE")))
                .andExpect(jsonPath("$.netSalary").value(6000.05));
    }

    @Test
    void repositoryScopesSameEmployeeAndCompetenceInBothDirections() {
        LocalDate competence = LocalDate.of(2026, 7, 1);

        var tenantA = repository.findByCompanyIdAndEmployeeIdAndPayrollDate(
                "tenant-a", "employee-1", competence).orElseThrow();
        var tenantB = repository.findByCompanyIdAndEmployeeIdAndPayrollDate(
                "tenant-b", "employee-1", competence).orElseThrow();

        org.assertj.core.api.Assertions.assertThat(tenantA.getNetSalary())
                .isEqualByComparingTo("8000.05");
        org.assertj.core.api.Assertions.assertThat(tenantB.getNetSalary())
                .isEqualByComparingTo("6000.05");
        org.assertj.core.api.Assertions.assertThat(
                repository.findByCompanyIdAndEmployeeIdAndPayrollDate(
                        "tenant-c", "employee-1", competence)).isEmpty();
    }

    @Test
    void crossTenantResourceIsIndistinguishableFromMissingResource() throws Exception {
        repository.delete(repository.findByCompanyIdAndEmployeeIdAndPayrollDate("tenant-a", "employee-1", LocalDate.of(2026, 7, 1)).orElseThrow());
        mvc.perform(get("/api/payroll/employee-1").param("year", "2026").param("month", "7")
                        .with(identity("tenant-a", "employee-1", "ROLE_EMPLOYEE")))
                .andExpect(status().isNotFound()).andExpect(content().string(""));
        repository.save(new PayrollJpaEntity(null, "tenant-a", "employee-1", LocalDate.of(2026, 7, 1),
                new BigDecimal("10000.10"), new BigDecimal("2000.05"), new BigDecimal("8000.05")));
        mvc.perform(get("/api/payroll/employee-1").param("year", "2026").param("month", "7")
                        .with(identity("tenant-a", "another-employee", "ROLE_EMPLOYEE")))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.error").value("Resource not found"));

        repository.delete(repository.findByCompanyIdAndEmployeeIdAndPayrollDate("tenant-b", "employee-1", LocalDate.of(2026, 7, 1)).orElseThrow());
        mvc.perform(get("/api/payroll/employee-1").param("year", "2026").param("month", "7")
                        .with(identity("tenant-b", "employee-1", "ROLE_EMPLOYEE")))
                .andExpect(status().isNotFound()).andExpect(content().string(""));
    }

    @Test
    void adminListIsTenantScoped() throws Exception {
        mvc.perform(get("/api/payroll").with(identity("tenant-a", "admin", "ROLE_ADMIN")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].netSalary").value(8000.05));
        mvc.perform(get("/payroll").with(identity("tenant-b", "admin", "ROLE_ADMIN")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].netSalary").value(6000.05));
        mvc.perform(get("/api/payroll").with(identity("tenant-a", "employee-1", "ROLE_EMPLOYEE")))
                .andExpect(status().isForbidden());
    }

    @Test
    void rejectsMissingTenantAndUnauthenticatedRequestsWithoutSensitiveDetails() throws Exception {
        mvc.perform(get("/api/payroll/employee-1").param("year", "2026").param("month", "7")
                        .with(jwt().jwt(j -> j.subject("user").claim("employeeId", "employee-1").claim("roles", List.of("ROLE_EMPLOYEE")))))
                .andExpect(status().isForbidden()).andExpect(jsonPath("$.error").value("Access denied"));
        mvc.perform(get("/api/payroll/employee-1").param("year", "2026").param("month", "7"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rejectsBlankTenantAndMissingAdminRoleWithoutFallback() throws Exception {
        mvc.perform(get("/api/payroll/employee-1").param("year", "2026").param("month", "7")
                        .with(identity("   ", "employee-1", "ROLE_EMPLOYEE")))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Access denied"));
        mvc.perform(get("/api/payroll")
                        .with(jwt().jwt(j -> j.subject("user").claim("companyId", "tenant-a"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void rejectsMalformedRolesClaimInRealToken() throws Exception {
        mvc.perform(get("/api/payroll")
                        .header("Authorization", "Bearer " + signedTokenWithPayload(
                                "{\"sub\":\"admin-user\",\"roles\":{\"unexpected\":\"ROLE_ADMIN\"},"
                                        + "\"companyId\":\"tenant-a\",\"employeeId\":\"admin\","
                                        + "\"iat\":" + Instant.now().getEpochSecond() + ",\"exp\":"
                                        + Instant.now().plusSeconds(300).getEpochSecond() + "}")))
                .andExpect(status().isForbidden());
    }

    @Test
    void acceptsRealHs512TokenCompatibleWithAuthServiceClaims() throws Exception {
        mvc.perform(get("/api/payroll/employee-1").param("year", "2026").param("month", "7")
                        .header("Authorization", "Bearer " + signedToken(
                                "tenant-a", "employee-1", Instant.now().plusSeconds(300))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.netSalary").value(8000.05));
    }

    @Test
    void rejectsExpiredAndInvalidRealTokens() throws Exception {
        mvc.perform(get("/api/payroll/employee-1").param("year", "2026").param("month", "7")
                        .header("Authorization", "Bearer " + signedToken(
                                "tenant-a", "employee-1", Instant.now().minusSeconds(30))))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/api/payroll/employee-1").param("year", "2026").param("month", "7")
                        .header("Authorization", "Bearer invalid.jwt.token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void invalidCompetenceReturnsSafeBadRequest() throws Exception {
        mvc.perform(get("/api/payroll/employee-1").param("year", "2026").param("month", "13")
                        .with(identity("tenant-a", "employee-1", "ROLE_EMPLOYEE")))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value("Invalid request"));
    }

    private static org.springframework.test.web.servlet.request.RequestPostProcessor identity(String company, String employee, String role) {
        return jwt().jwt(j -> j.subject("user").claim("companyId", company).claim("employeeId", employee)
                .claim("roles", List.of(role))).authorities(() -> role);
    }

    private static String signedToken(String companyId, String employeeId, Instant expiresAt) throws Exception {
        return signedTokenWithPayload("{\"sub\":\"employee-user\",\"roles\":[\"ROLE_EMPLOYEE\"],"
                + "\"companyId\":\"" + companyId + "\",\"employeeId\":\"" + employeeId
                + "\",\"iat\":" + Instant.now().getEpochSecond() + ",\"exp\":"
                + expiresAt.getEpochSecond() + "}");
    }

    private static String signedTokenWithPayload(String payloadJson) throws Exception {
        String header = base64Url("{\"alg\":\"HS512\",\"typ\":\"JWT\"}");
        String payload = base64Url(payloadJson);
        String signingInput = header + "." + payload;
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(new SecretKeySpec(JWT_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA512"));
        return signingInput + "." + Base64.getUrlEncoder().withoutPadding()
                .encodeToString(mac.doFinal(signingInput.getBytes(StandardCharsets.US_ASCII)));
    }

    private static String base64Url(String value) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
}
