import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

@WebMvcTest(PayrollController.class)
public class PayrollControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws Exception {
        // Setup code here (if necessary)
    }

    @Test
    public void testCreatePayroll() throws Exception {
        String payrollJson = "{ \"name\": \"Test Payroll\", \"amount\": 1000 }";
        mockMvc.perform(post("/payrolls")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payrollJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetPayroll() throws Exception {
        mockMvc.perform(get("/payrolls/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Payroll"));
    }

    @Test
    public void testUpdatePayroll() throws Exception {
        String payrollJson = "{ \"name\": \"Updated Payroll\", \"amount\": 1500 }";
        mockMvc.perform(put("/payrolls/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payrollJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeletePayroll() throws Exception {
        mockMvc.perform(delete("/payrolls/{id}", 1))
                .andExpect(status().isNoContent());
    }

}