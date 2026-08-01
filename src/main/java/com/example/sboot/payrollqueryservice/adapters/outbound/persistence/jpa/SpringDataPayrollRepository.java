package com.thallyson.sboot.payrollqueryservice.adapters.outbound.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataPayrollRepository extends JpaRepository<PayrollJpaEntity, Long> {
    Optional<PayrollJpaEntity> findByCompanyIdAndEmployeeIdAndPayrollDate(String companyId, String employeeId, LocalDate payrollDate);
    List<PayrollJpaEntity> findAllByCompanyId(String companyId);
}
