package com.thallyson.sboot.payrollqueryservice.adapters.outbound.persistence.jpa;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payroll")
public class PayrollJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private String employeeId;

    @Column(name = "payroll_date", nullable = false)
    private LocalDate payrollDate;

    @Column(name = "gross_salary", nullable = false)
    private BigDecimal grossSalary;

    @Column(name = "deductions", nullable = false)
    private BigDecimal deductions;

    @Column(name = "net_salary", nullable = false)
    private BigDecimal netSalary;

    public PayrollJpaEntity() {
    }

    public PayrollJpaEntity(Long id, String employeeId, LocalDate payrollDate, BigDecimal grossSalary, BigDecimal deductions, BigDecimal netSalary) {
        this.id = id;
        this.employeeId = employeeId;
        this.payrollDate = payrollDate;
        this.grossSalary = grossSalary;
        this.deductions = deductions;
        this.netSalary = netSalary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getPayrollDate() {
        return payrollDate;
    }

    public void setPayrollDate(LocalDate payrollDate) {
        this.payrollDate = payrollDate;
    }

    public BigDecimal getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(BigDecimal grossSalary) {
        this.grossSalary = grossSalary;
    }

    public BigDecimal getDeductions() {
        return deductions;
    }

    public void setDeductions(BigDecimal deductions) {
        this.deductions = deductions;
    }

    public BigDecimal getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(BigDecimal netSalary) {
        this.netSalary = netSalary;
    }
}
