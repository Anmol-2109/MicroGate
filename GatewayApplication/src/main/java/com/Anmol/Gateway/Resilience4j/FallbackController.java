package com.Anmol.Gateway.Resilience4j;


import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/fallback")
@Slf4j
@RequiredArgsConstructor
public class FallbackController {

    private final MeterRegistry meterRegistry;

    @RequestMapping("/{service}")
    public ResponseEntity<?> fallback(
            @PathVariable String service
    ) {
        log.error("CIRCUIT BREAKER FALLBACK EXECUTED");
        meterRegistry.counter(
                "gateway_circuitbreaker_open"
        ).increment();
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(
                        Map.of(
                                "success", false,
                                "service", service,
                                "message",
                                service +
                                        " service temporarily unavailable"
                        )
                );
    }
}