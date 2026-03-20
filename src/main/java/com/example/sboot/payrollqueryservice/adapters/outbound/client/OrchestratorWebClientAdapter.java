package com.thallyson.sboot.payrollqueryservice.adapters.outbound.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.thallyson.sboot.payrollqueryservice.domain.ports.outbound.OrchestratorClientPort;

@Component
public class OrchestratorWebClientAdapter implements OrchestratorClientPort {
    private final WebClient webClient;

    public OrchestratorWebClientAdapter(WebClient webClient) {
        this.webClient = webClient;
    }

    // Implement methods from OrchestratorClientPort here
}
