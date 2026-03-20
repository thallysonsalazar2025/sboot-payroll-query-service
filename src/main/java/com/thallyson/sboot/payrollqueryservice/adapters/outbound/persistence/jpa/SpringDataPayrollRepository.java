import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataPayrollRepository extends JpaRepository<Payroll, Long> {
    // Custom query methods can be defined here
}