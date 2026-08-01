package com.thallyson.sboot.payrollqueryservice.domain.ports.outbound;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import com.thallyson.sboot.payrollqueryservice.domain.entity.Payroll;

public interface PayrollRepositoryPort {
    void savePayroll(Payroll payroll);
    Optional<Payroll> findByCompanyIdAndEmployeeIdAndPayrollDate(String companyId, String employeeId, LocalDate payrollDate);
    List<Payroll> findAllByCompanyId(String companyId);
}
