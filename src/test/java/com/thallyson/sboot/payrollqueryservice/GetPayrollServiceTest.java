import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class GetPayrollServiceTest {

    @InjectMocks
    private PayrollService payrollService;

    @Mock
    private PayrollRepository payrollRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPayrollSuccess() {
        // Arrange
        String document = "validDocument";
        Payroll expectedPayroll = new Payroll();  // assume Payroll is a valid class
        when(payrollRepository.findByDocument(document)).thenReturn(expectedPayroll);

        // Act
        Payroll actualPayroll = payrollService.getPayroll(document);

        // Assert
        assertEquals(expectedPayroll, actualPayroll);
    }

    @Test
    public void testGetPayrollInvalidStatus() {
        // Arrange
        String document = "invalidDocument";
        when(payrollRepository.findByDocument(document)).thenReturn(null);

        // Act
        Payroll actualPayroll = payrollService.getPayroll(document);

        // Assert
        assertNull(actualPayroll);
    }

    @Test
    public void testGetPayrollNullDocument() {
        // Act
        Payroll actualPayroll = payrollService.getPayroll(null);

        // Assert
        assertNull(actualPayroll);
    }
}