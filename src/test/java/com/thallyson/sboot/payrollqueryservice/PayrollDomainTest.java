package com.thallyson.sboot.payrollqueryservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.thallyson.sboot.payrollqueryservice.domain.entity.Payroll;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PayrollDomainTest {
    @Test
    void computesMoneyWithBigDecimalWithoutBinaryRounding() {
        Payroll payroll = new Payroll(null, "tenant-a", "employee-1", LocalDate.of(2026, 7, 1),
                new BigDecimal("10000.10"), new BigDecimal("2000.05"));
        assertEquals(new BigDecimal("8000.05"), payroll.getNetSalary());
    }

    @Test
    void rejectsMissingOwnership() {
        assertThrows(IllegalArgumentException.class, () -> new Payroll(null, " ", "employee-1",
                LocalDate.of(2026, 7, 1), BigDecimal.ONE, BigDecimal.ZERO));
    }
}
