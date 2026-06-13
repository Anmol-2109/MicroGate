package com.Anmol.Gateway.LoadBalancer;


import com.Anmol.Gateway.Entity.ServiceEntity;
import com.Anmol.Gateway.Service.DiscoveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadBalancerManager {

    private final DiscoveryService
            discoveryService;

    private final LoadBalancerService
            loadBalancerService;

    public ServiceEntity resolve(
            String serviceName
    ) {

        return loadBalancerService.choose(
                serviceName ,
                discoveryService
                        .getHealthyInstances(
                                serviceName
                        )
        );
    }
}