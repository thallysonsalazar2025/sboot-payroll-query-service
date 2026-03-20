package com.thallyson.sboot.payrollqueryservice.adapters.outbound.persistence.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Optional<Payroll> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Payroll> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private PayrollJpaEntity toEntity(Payroll payroll) {
        return new PayrollJpaEntity(
                payroll.getId(),
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
                entity.getEmployeeId(),
                entity.getPayrollDate(),
                entity.getGrossSalary(),
                entity.getDeductions()
        );
    }
}
