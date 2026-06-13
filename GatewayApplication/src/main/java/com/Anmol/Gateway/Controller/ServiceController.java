package com.Anmol.Gateway.Controller;


import com.Anmol.Gateway.DTO.HeartbeatRequestDto;
import com.Anmol.Gateway.DTO.ServiceRequestDto;
import com.Anmol.Gateway.Entity.ServiceEntity;
import com.Anmol.Gateway.Service.DiscoveryService;
import com.Anmol.Gateway.Service.ServiceRegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceRegistryService service;
    private final DiscoveryService discoveryService;

    @PostMapping
    public ServiceEntity create(
            @RequestBody ServiceRequestDto dto
    ) {
        return service.create(dto);
    }

    @GetMapping
    public List<ServiceEntity> getAll() {
        return service.getAll();
    }

    @PostMapping("/heartbeat")
    public void heartbeat(
            @RequestBody
            HeartbeatRequestDto dto
    ) {

        service.heartbeat(
                dto.getInstanceId()
        );
    }

    @GetMapping(
            "/discover/{serviceName}"
    )
    public List<ServiceEntity> discover(
            @PathVariable
            String serviceName
    ) {

        return discoveryService
                .getHealthyInstances(
                        serviceName
                );
    }
}