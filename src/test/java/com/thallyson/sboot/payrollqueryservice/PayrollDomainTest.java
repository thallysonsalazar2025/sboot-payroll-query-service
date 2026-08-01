package com.thallyson.sboot.payrollqueryservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.thallyson.sboot.payrollqueryservice.domain.entity.Payroll;
import com.thallyson.sboot.payrollqueryservice.adapters.outbound.persistence.jpa.PayrollJpaEntity;
import com.thallyson.sboot.payrollqueryservice.adapters.outbound.persistence.jpa.PersistencePayrollRepositoryAdapter;
import com.thallyson.sboot.payrollqueryservice.adapters.outbound.persistence.jpa.SpringDataPayrollRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class PayrollDomainTest {
    @Test
    void computesMoneyWithBigDecimalWithoutBinaryRounding() {
        Payroll payroll = new Payroll(null, "tenant-a", "employee-1", LocalDate.of(2026, 7, 1),
                new BigDecimal("10000.10"), new BigDecimal("2000.05"));
        assertEquals("tenant-a", payroll.getCompanyId());
        assertEquals(new BigDecimal("8000.05"), payroll.getNetSalary());
    }

    @Test
    void rejectsMissingOwnership() {
        assertThrows(IllegalArgumentException.class, () -> new Payroll(null, " ", "employee-1",
                LocalDate.of(2026, 7, 1), BigDecimal.ONE, BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> new Payroll(null, null, "employee-1",
                LocalDate.of(2026, 7, 1), BigDecimal.ONE, BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> new Payroll(null, "tenant-a", " ",
                LocalDate.of(2026, 7, 1), BigDecimal.ONE, BigDecimal.ZERO));
    }

    @Test
    void supportsJpaTenantAssignmentDuringBackfill() {
        PayrollJpaEntity entity = new PayrollJpaEntity();
        entity.setCompanyId("tenant-a");
        assertEquals("tenant-a", entity.getCompanyId());
    }

    @Test
    void persistenceAdapterWritesEveryFinancialAndTenantField() {
        SpringDataPayrollRepository repository = mock(SpringDataPayrollRepository.class);
        Payroll payroll = new Payroll(7L, "tenant-a", "employee-1", LocalDate.of(2026, 7, 31),
                new BigDecimal("10000.00"), new BigDecimal("2000.00"), new BigDecimal("7777.77"));

        new PersistencePayrollRepositoryAdapter(repository).savePayroll(payroll);

        ArgumentCaptor<PayrollJpaEntity> captor = ArgumentCaptor.forClass(PayrollJpaEntity.class);
        verify(repository).save(captor.capture());
        PayrollJpaEntity saved = captor.getValue();
        assertEquals("tenant-a", saved.getCompanyId());
        assertEquals(new BigDecimal("7777.77"), saved.getNetSalary());
    }
}
