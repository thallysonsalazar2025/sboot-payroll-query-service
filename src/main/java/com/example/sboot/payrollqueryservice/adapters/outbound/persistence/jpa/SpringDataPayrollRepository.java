package com.thallyson.sboot.payrollqueryservice.adapters.outbound.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataPayrollRepository extends JpaRepository<PayrollJpaEntity, Long> {
    // Custom query methods can be defined here
}
