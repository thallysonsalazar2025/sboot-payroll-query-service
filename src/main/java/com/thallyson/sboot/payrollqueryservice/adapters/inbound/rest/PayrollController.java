package com.thallyson.sboot.payrollqueryservice.adapters.inbound.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayrollController {

    @GetMapping("/payroll")
    public String getPayroll() {
        return "Payroll information";
    }
}