package com.Anmol.Gateway.Service;


import com.Anmol.Gateway.Entity.ServiceEntity;
import com.Anmol.Gateway.Repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscoveryService {

    private final ServiceRepository repository;

    public List<ServiceEntity> getHealthyInstances(
            String serviceName
    ) {

        return repository
                .findByServiceNameAndStatus(
                        serviceName,
                        "UP"
                );
    }
}