package com.thallyson.sboot.payrollqueryservice.application.usecase;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.thallyson.sboot.payrollqueryservice.application.usecase.impl.GetPayrollService;
import com.thallyson.sboot.payrollqueryservice.domain.entity.Payroll;
import com.thallyson.sboot.payrollqueryservice.domain.ports.outbound.PayrollRepositoryPort;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetPayrollServiceTest {
    @Mock PayrollRepositoryPort repository;

    @Test
    void scopesSingleAndListQueriesToTenant() {
        Payroll payroll = new Payroll(1L, "tenant-a", "employee-1", LocalDate.of(2026, 7, 1),
                new BigDecimal("10.00"), new BigDecimal("1.00"));
        when(repository.findByCompanyIdAndEmployeeIdAndPayrollDateBetween(
                "tenant-a", "employee-1", LocalDate.of(2026, 7, 1), LocalDate.of(2026, 8, 1)))
                .thenReturn(List.of(payroll));
        when(repository.findAllByCompanyId("tenant-a")).thenReturn(List.of(payroll));
        GetPayrollService service = new GetPayrollService(repository);

        assertSame(payroll, service.get("tenant-a", "employee-1", YearMonth.of(2026, 7)).orElseThrow());
        assertSame(payroll, service.list("tenant-a").get(0));
        verify(repository).findByCompanyIdAndEmployeeIdAndPayrollDateBetween(
                "tenant-a", "employee-1", LocalDate.of(2026, 7, 1), LocalDate.of(2026, 8, 1));
        verify(repository).findAllByCompanyId("tenant-a");
    }

    @Test
    void keepsSymmetricTenantQueriesSeparatedForTheSameEmployeeAndCompetence() {
        LocalDate competence = LocalDate.of(2026, 7, 1);
        Payroll tenantA = new Payroll(1L, "tenant-a", "employee-1", competence,
                new BigDecimal("10.00"), new BigDecimal("1.00"));
        Payroll tenantB = new Payroll(2L, "tenant-b", "employee-1", competence,
                new BigDecimal("20.00"), new BigDecimal("2.00"));
        when(repository.findByCompanyIdAndEmployeeIdAndPayrollDateBetween(
                "tenant-a", "employee-1", competence, LocalDate.of(2026, 8, 1)))
                .thenReturn(List.of(tenantA));
        when(repository.findByCompanyIdAndEmployeeIdAndPayrollDateBetween(
                "tenant-b", "employee-1", competence, LocalDate.of(2026, 8, 1)))
                .thenReturn(List.of(tenantB));
        GetPayrollService service = new GetPayrollService(repository);

        assertSame(tenantA, service.get("tenant-a", "employee-1", YearMonth.of(2026, 7)).orElseThrow());
        assertSame(tenantB, service.get("tenant-b", "employee-1", YearMonth.of(2026, 7)).orElseThrow());
        verify(repository).findByCompanyIdAndEmployeeIdAndPayrollDateBetween(
                "tenant-a", "employee-1", competence, LocalDate.of(2026, 8, 1));
        verify(repository).findByCompanyIdAndEmployeeIdAndPayrollDateBetween(
                "tenant-b", "employee-1", competence, LocalDate.of(2026, 8, 1));
    }
}
