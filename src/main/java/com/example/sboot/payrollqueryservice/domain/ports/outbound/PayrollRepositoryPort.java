package com.thallyson.sboot.payrollqueryservice.domain.ports.outbound;

import java.util.List;
import java.util.Optional;

import com.thallyson.sboot.payrollqueryservice.domain.entity.Payroll;

public interface PayrollRepositoryPort {
    void savePayroll(Payroll payroll);
    Optional<Payroll> findById(Long id);
    List<Payroll> findAll();
}
