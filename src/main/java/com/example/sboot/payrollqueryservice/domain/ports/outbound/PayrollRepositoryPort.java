package com.thallyson.sboot.payrollqueryservice.domain.ports.outbound;

import java.util.List;
import java.time.LocalDate;

import com.thallyson.sboot.payrollqueryservice.domain.entity.Payroll;

public interface PayrollRepositoryPort {
    void savePayroll(Payroll payroll);
    List<Payroll> findByCompanyIdAndEmployeeIdAndPayrollDateBetween(
            String companyId, String employeeId, LocalDate startInclusive, LocalDate endExclusive);
    List<Payroll> findAllByCompanyId(String companyId);
}
