package com.thallyson.sboot.payrollqueryservice.adapters.outbound.persistence.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDate;

import org.springframework.stereotype.Repository;

import com.thallyson.sboot.payrollqueryservice.domain.entity.Payroll;
import com.thallyson.sboot.payrollqueryservice.domain.ports.outbound.PayrollRepositoryPort;

@Repository
public class PersistencePayrollRepositoryAdapter implements PayrollRepositoryPort {

    private final SpringDataPayrollRepository repository;

    public PersistencePayrollRepositoryAdapter(SpringDataPayrollRepository repository) {
        this.repository = repository;
    }

    @Override
    public void savePayroll(Payroll payroll) {
        repository.save(toEntity(payroll));
    }

    @Override
    public Optional<Payroll> findByCompanyIdAndEmployeeIdAndPayrollDate(String companyId, String employeeId, LocalDate payrollDate) {
        return repository.findByCompanyIdAndEmployeeIdAndPayrollDate(companyId, employeeId, payrollDate).map(this::toDomain);
    }

    @Override
    public List<Payroll> findAllByCompanyId(String companyId) {
        return repository.findAllByCompanyId(companyId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private PayrollJpaEntity toEntity(Payroll payroll) {
        return new PayrollJpaEntity(
                payroll.getId(),
                payroll.getCompanyId(),
                payroll.getEmployeeId(),
                payroll.getPayrollDate(),
                payroll.getGrossSalary(),
                payroll.getDeductions(),
                payroll.getNetSalary()
        );
    }

    private Payroll toDomain(PayrollJpaEntity entity) {
        return new Payroll(
                entity.getId(),
                entity.getCompanyId(),
                entity.getEmployeeId(),
                entity.getPayrollDate(),
                entity.getGrossSalary(),
                entity.getDeductions()
        );
    }
}
