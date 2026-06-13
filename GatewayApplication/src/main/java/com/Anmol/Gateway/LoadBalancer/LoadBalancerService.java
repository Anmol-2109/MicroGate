package com.Anmol.Gateway.LoadBalancer;


import com.Anmol.Gateway.Entity.ServiceEntity;

import java.util.List;

public interface LoadBalancerService {

    ServiceEntity choose(String serviceName,
            List<ServiceEntity> instances
    );
}