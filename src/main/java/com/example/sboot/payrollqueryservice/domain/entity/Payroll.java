package com.thallyson.sboot.payrollqueryservice.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class Payroll {
    private final Long id;
    private final String employeeId;
    private final LocalDate payrollDate;
    private final BigDecimal grossSalary;
    private final BigDecimal deductions;
    private final BigDecimal netSalary;

    public Payroll(Long id, String employeeId, LocalDate payrollDate, BigDecimal grossSalary, BigDecimal deductions) {
        this.id = id;
        this.employeeId = employeeId;
        this.payrollDate = payrollDate;
        this.grossSalary = grossSalary;
        this.deductions = deductions;
        this.netSalary = grossSalary.subtract(deductions);
    }

    public Long getId() {
        return id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public LocalDate getPayrollDate() {
        return payrollDate;
    }

    public BigDecimal getGrossSalary() {
        return grossSalary;
    }

    public BigDecimal getDeductions() {
        return deductions;
    }

    public BigDecimal getNetSalary() {
        return netSalary;
    }
}