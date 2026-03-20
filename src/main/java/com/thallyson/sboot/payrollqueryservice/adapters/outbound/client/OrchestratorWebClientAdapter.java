package com.thallyson.sboot.payrollqueryservice.adapters.outbound.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OrchestratorWebClientAdapter implements OrchestratorClientPort {
    private final WebClient webClient;

    public OrchestratorWebClientAdapter(WebClient webClient) {
        this.webClient = webClient;
    }

    // Implement methods from OrchestratorClientPort here
}