package com.thallyson.sboot.payrollqueryservice.application.usecase;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import com.thallyson.sboot.payrollqueryservice.domain.entity.Payroll;

public interface GetPayrollUseCase {
    Optional<Payroll> get(String companyId, String employeeId, YearMonth competence);
    List<Payroll> list(String companyId);
}
