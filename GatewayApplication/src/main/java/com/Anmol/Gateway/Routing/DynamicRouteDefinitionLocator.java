package com.Anmol.Gateway.Routing;

import com.Anmol.Gateway.Entity.RouteEntity;
import com.Anmol.Gateway.Repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DynamicRouteDefinitionLocator
        implements RouteDefinitionLocator {

    private final RouteRepository routeRepository;

    private final RouteDefinitionMapper mapper;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {

        List<RouteEntity> routes =
                routeRepository.findAll();

        log.info("Loaded {} routes from DB",
                routes.size());

        routes.forEach(route ->
                log.info(
                        "Route {} -> {}",
                        route.getPathPattern(),
                        route.getServiceName()
                )
        );

        return Flux.fromIterable(routes)
                .filter(RouteEntity::getEnabled)
                .map(mapper::map);
    }
}