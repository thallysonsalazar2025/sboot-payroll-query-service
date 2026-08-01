package com.thallyson.sboot.payrollqueryservice.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class Payroll {
    private final Long id;
    private final String companyId;
    private final String employeeId;
    private final LocalDate payrollDate;
    private final BigDecimal grossSalary;
    private final BigDecimal deductions;
    private final BigDecimal netSalary;

    public Payroll(Long id, String companyId, String employeeId, LocalDate payrollDate, BigDecimal grossSalary, BigDecimal deductions) {
        this(id, companyId, employeeId, payrollDate, grossSalary, deductions, grossSalary.subtract(deductions));
    }

    public Payroll(Long id, String companyId, String employeeId, LocalDate payrollDate, BigDecimal grossSalary,
                   BigDecimal deductions, BigDecimal netSalary) {
        this.id = id;
        this.companyId = requireText(companyId, "companyId");
        this.employeeId = requireText(employeeId, "employeeId");
        this.payrollDate = payrollDate;
        this.grossSalary = grossSalary;
        this.deductions = deductions;
        this.netSalary = netSalary;
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value;
    }

    public String getCompanyId() { return companyId; }

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
