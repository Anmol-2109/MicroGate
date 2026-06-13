package com.Anmol.Gateway.Repository;



import com.Anmol.Gateway.Entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository
        extends JpaRepository<ServiceEntity, Long> {


    Optional<ServiceEntity>
    findByInstanceId(String instanceId);

    List<ServiceEntity>
    findByServiceNameAndStatus(
            String serviceName,
            String status
    );
}