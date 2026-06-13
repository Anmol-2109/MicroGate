package com.Anmol.Gateway.Service;



import com.Anmol.Gateway.DTO.RouteRequestDto;
import com.Anmol.Gateway.Entity.RouteEntity;
import com.Anmol.Gateway.Entity.ServiceEntity;
import com.Anmol.Gateway.Repository.RouteRepository;
import com.Anmol.Gateway.Repository.ServiceRepository;
import com.Anmol.Gateway.Routing.RouteRefreshService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteRegistryService {

    private final RouteRepository routeRepository;
    private final ServiceRepository serviceRepository;
    private final RouteRefreshService refreshService;

    public RouteEntity createRoute(
            RouteRequestDto dto
    ) {

        RouteEntity route =
                RouteEntity.builder()
                        .pathPattern(dto.getPathPattern())
                        .enabled(dto.getEnabled())
                        .serviceName(dto.getServiceName())
                        .createdAt(LocalDateTime.now())
                        .build();

        RouteEntity saved =
                routeRepository.save(route);

        refreshService.refreshRoutes();

        return saved;
    }

    public List<RouteEntity> getAllRoutes() {

        return routeRepository.findAll();
    }
}