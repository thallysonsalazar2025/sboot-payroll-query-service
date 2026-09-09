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
        List<Payroll> payrolls = repository.findByCompanyIdAndEmployeeIdAndPayrollDateBetween(
                companyId, employeeId, competence.atDay(1), competence.plusMonths(1).atDay(1));
        if (payrolls.size() > 1) {
            throw new IllegalStateException("Multiple payroll records found for the requested competence");
        }
        return payrolls.stream().findFirst();
    }

    public List<Payroll> list(String companyId) { return repository.findAllByCompanyId(companyId); }
}
