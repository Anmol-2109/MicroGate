package com.Anmol.Gateway.Service;

import com.Anmol.Gateway.Entity.ServiceEntity;
import com.Anmol.Gateway.Repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthMonitorService {

    private final ServiceRepository repository;

    public void monitorHealth() {

        List<ServiceEntity> services =
                repository.findAll();

        LocalDateTime now =
                LocalDateTime.now();

        for(ServiceEntity service : services)
        {

            if(service.getLastHeartbeat() == null)
            {
                continue;
            }

            long seconds =
                    Duration.between(
                            service.getLastHeartbeat(),
                            now
                    ).getSeconds();

            if(seconds > 30)
            {
                service.setStatus("DOWN");

                repository.save(service);
            }
        }
    }
}