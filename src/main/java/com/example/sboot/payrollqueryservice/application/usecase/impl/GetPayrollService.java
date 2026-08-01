package com.thallyson.sboot.payrollqueryservice.application.usecase.impl;

import com.thallyson.sboot.payrollqueryservice.application.usecase.GetPayrollUseCase;
import com.thallyson.sboot.payrollqueryservice.domain.entity.Payroll;
import com.thallyson.sboot.payrollqueryservice.domain.ports.outbound.PayrollRepositoryPort;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GetPayrollService implements GetPayrollUseCase {
    private final PayrollRepositoryPort repository;

    public GetPayrollService(PayrollRepositoryPort repository) { this.repository = repository; }

    public Optional<Payroll> get(String companyId, String employeeId, YearMonth competence) {
        return repository.findByCompanyIdAndEmployeeIdAndPayrollDate(companyId, employeeId, competence.atDay(1));
    }

    public List<Payroll> list(String companyId) { return repository.findAllByCompanyId(companyId); }
}
