package com.Anmol.Gateway.Schedular;


import com.Anmol.Gateway.Service.HealthMonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceHealthScheduler {

    private final HealthMonitorService
            healthMonitorService;

    @Scheduled(
            fixedRate = 30000
    )
    public void monitorServices()
    {
        healthMonitorService
                .monitorHealth();
    }
}