package com.Anmol.Gateway.LoadBalancer;



import com.Anmol.Gateway.Entity.ServiceEntity;
import com.Anmol.Gateway.Exception.NoHealthyInstanceException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RoundRobinLoadBalancer
        implements LoadBalancerService {

    private final AtomicInteger counter =
            new AtomicInteger(0);

    @Override
    public ServiceEntity choose(
            String serviceName ,
            List<ServiceEntity> instances
    ) {

        if(instances.isEmpty())
        {
            throw new NoHealthyInstanceException(
                serviceName
        );
        }

        int index =
                Math.abs(
                        counter.getAndIncrement()
                ) % instances.size();

        return instances.get(index);
    }
}