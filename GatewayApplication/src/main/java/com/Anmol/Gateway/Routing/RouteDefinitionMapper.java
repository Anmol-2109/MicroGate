package com.Anmol.Gateway.Routing;


import com.Anmol.Gateway.Entity.RouteEntity;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Collections;
import java.util.List;


@Component
public class RouteDefinitionMapper {

    public RouteDefinition map(
            RouteEntity route
    ) {

        RouteDefinition definition =
                new RouteDefinition();

        definition.setId(
                route.getId().toString()
        );

        definition.setUri(
                URI.create(
                        "lb://" +
                                route.getServiceName()
                )
        );

        PredicateDefinition predicate =
                new PredicateDefinition();

        predicate.setName("Path");

        predicate.addArg(
                NameUtils.generateName(0),
                route.getPathPattern()
        );

        definition.setPredicates(
                List.of(predicate)
        );

        FilterDefinition circuitBreaker = new FilterDefinition();

        circuitBreaker.setName(
                "CircuitBreaker"
        );

        circuitBreaker.addArg(
                "name",
                route.getServiceName()
        );

        circuitBreaker.addArg(
                "fallbackUri",
                "forward:/fallback/"
                        + route.getServiceName()
        );

        FilterDefinition retry =
                new FilterDefinition();

        retry.setName("Retry");

        retry.addArg(
                "retries",
                "2"
        );

        retry.addArg(
                "statuses",
                "INTERNAL_SERVER_ERROR,BAD_GATEWAY,GATEWAY_TIMEOUT,SERVICE_UNAVAILABLE"
        );

        FilterDefinition stripPrefix =
                new FilterDefinition();

        stripPrefix.setName("StripPrefix");

        stripPrefix.addArg(
                NameUtils.generateName(0),
                "1"
        );



        if(route.getServiceName().equals("USER-SERVICE"))
        {
            definition.setFilters(
                    List.of(
                            stripPrefix
                    )
            );
        }
        else
        {
            definition.setFilters(
                    List.of(
                            stripPrefix,
                            retry,
                            circuitBreaker
                    )
            );
        }

        return definition;
    }
}