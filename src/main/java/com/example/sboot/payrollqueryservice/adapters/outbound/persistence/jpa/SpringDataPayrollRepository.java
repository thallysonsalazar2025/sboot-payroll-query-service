package com.thallyson.sboot.payrollqueryservice.adapters.outbound.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SpringDataPayrollRepository extends JpaRepository<PayrollJpaEntity, Long> {
    List<PayrollJpaEntity> findByCompanyIdAndEmployeeIdAndPayrollDateGreaterThanEqualAndPayrollDateLessThan(
            String companyId, String employeeId, LocalDate startInclusive, LocalDate endExclusive);
    List<PayrollJpaEntity> findAllByCompanyId(String companyId);
}
