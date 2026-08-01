package com.thallyson.sboot.payrollqueryservice.application.dto;

import java.math.BigDecimal;

public record PayrollResponseDto(String employeeId, int year, int month,
                                 BigDecimal grossSalary, BigDecimal deductions, BigDecimal netSalary) {}
