package com.thallyson.sboot.payrollqueryservice.adapters.inbound.rest;

import com.thallyson.sboot.payrollqueryservice.application.dto.PayrollResponseDto;
import com.thallyson.sboot.payrollqueryservice.application.usecase.GetPayrollUseCase;
import com.thallyson.sboot.payrollqueryservice.domain.entity.Payroll;
import com.thallyson.sboot.payrollqueryservice.security.TenantIdentity;
import com.thallyson.sboot.payrollqueryservice.adapters.inbound.rest.exception.ResourceNotFoundException;
import java.time.YearMonth;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayrollController {
    private final GetPayrollUseCase useCase;

    public PayrollController(GetPayrollUseCase useCase) { this.useCase = useCase; }

    @GetMapping("/api/payroll/{employeeId}")
    public ResponseEntity<PayrollResponseDto> get(@PathVariable String employeeId,
            @RequestParam int year, @RequestParam int month, @AuthenticationPrincipal Jwt jwt) {
        if (!TenantIdentity.canReadEmployee(jwt, employeeId)) throw new ResourceNotFoundException();
        String companyId = TenantIdentity.companyId(jwt);
        YearMonth competence = YearMonth.of(year, month);
        return useCase.get(companyId, employeeId, competence).map(this::response)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping({"/api/payroll", "/payroll"})
    @PreAuthorize("hasRole('ADMIN')")
    public List<PayrollResponseDto> list(@AuthenticationPrincipal Jwt jwt) {
        return useCase.list(TenantIdentity.companyId(jwt)).stream().map(this::response).toList();
    }

    private PayrollResponseDto response(Payroll payroll) {
        return new PayrollResponseDto(payroll.getEmployeeId(), payroll.getPayrollDate().getYear(),
                payroll.getPayrollDate().getMonthValue(), payroll.getGrossSalary(), payroll.getDeductions(), payroll.getNetSalary());
    }
}
