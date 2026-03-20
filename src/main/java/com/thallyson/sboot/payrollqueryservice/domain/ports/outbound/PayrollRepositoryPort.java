package com.thallyson.sboot.payrollqueryservice.domain.ports.outbound;

public interface PayrollRepositoryPort {
    // Define the methods that the repository will implement
    void savePayroll(Payroll payroll);
    Payroll findById(Long id);
    List<Payroll> findAll();
}