package com.Anmol.Gateway.Service;



import com.Anmol.Gateway.DTO.ServiceRequestDto;
import com.Anmol.Gateway.Entity.ServiceEntity;
import com.Anmol.Gateway.Repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceRegistryService {

    private final ServiceRepository repository;

    public ServiceEntity create(ServiceRequestDto dto) {

        String port =
                URI.create(dto.getServiceUrl())
                        .getPort() + "";

        String instanceId =
                dto.getServiceName().toLowerCase()
                        + "-" + port;

        ServiceEntity entity =
                ServiceEntity.builder()
                        .serviceName(dto.getServiceName())
                        .instanceId(instanceId)
                        .serviceUrl(dto.getServiceUrl())
                        .serviceVersion(dto.getServiceVersion())
                        .status("UP")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .lastHeartbeat(LocalDateTime.now())
                        .build();

        return repository.save(entity);
    }

    public List<ServiceEntity> getAll() {
        return repository.findAll();
    }

    public void heartbeat(
            String instanceId
    ) {

        ServiceEntity service =
                repository
                        .findByInstanceId(instanceId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Instance Not Found"
                                )
                        );

        service.setStatus("UP");

        service.setLastHeartbeat(
                LocalDateTime.now()
        );

        repository.save(service);
    }
}