import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class GetPayrollServiceTest {

    @Mock
    private PayrollRepository payrollRepository; // Mocking the repository

    @InjectMocks
    private GetPayrollService getPayrollService; // Service under test

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPayrollById() {
        // Arrange
        Long payrollId = 1L;
        Payroll mockPayroll = new Payroll(payrollId, "salary", ...);
        when(payrollRepository.findById(payrollId)).thenReturn(Optional.of(mockPayroll));

        // Act
        Payroll payroll = getPayrollService.getPayrollById(payrollId);

        // Assert
        assertNotNull(payroll);
        assertEquals(mockPayroll.getId(), payroll.getId());
    }

    @Test
    void testDeletePayroll() {
        // Arrange
        Long payrollId = 1L;
        doNothing().when(payrollRepository).deleteById(any(Long.class));

        // Act
        getPayrollService.deletePayroll(payrollId);

        // Assert
        verify(payrollRepository).deleteById(payrollId);
    }

    // Additional tests can be added here
}
