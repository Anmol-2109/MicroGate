package com.Anmol.Gateway.Filter;

import com.Anmol.Gateway.Entity.ServiceEntity;
import com.Anmol.Gateway.LoadBalancer.LoadBalancerManager;
import com.Anmol.Gateway.Tracing.RequestTraceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLoadBalancerFilter
        implements GlobalFilter, Ordered {

    private final LoadBalancerManager
            loadBalancerManager;

    @Override
    public Mono<Void> filter(
            org.springframework.web.server.ServerWebExchange exchange,
            org.springframework.cloud.gateway.filter.GatewayFilterChain chain
    ) {

        URI uri =
                exchange.getAttribute(
                        ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR
                );

        log.info(
                "Forwarding To URI = {}",
                exchange.getRequest().getURI()
        );

        if(uri == null)
        {
            return chain.filter(exchange);
        }

        if(!"lb".equals(uri.getScheme()))
        {
            return chain.filter(exchange);
        }

        String serviceName =
                uri.getHost();

        RequestTraceContext context =
                exchange.getAttribute(
                        "traceContext"
                );

        if(context != null)
        {
            context.setServiceName(
                    serviceName
            );
        }


        Route route =
                exchange.getAttribute(
                        ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR
                );

        if(route != null && context != null)
        {
            context.setRouteId(
                    route.getId()
            );

            log.info(
                    "Route Matched {}",
                    route.getId()
            );
        }

        ServiceEntity instance =
                loadBalancerManager.resolve(
                        serviceName
                );

        URI newUri =
                URI.create(
                        instance.getServiceUrl()
                                +
                                exchange.getRequest()
                                        .getURI()
                                        .getRawPath()
                );



        if(context != null)
        {
            context.setSelectedInstance(
                    instance.getInstanceId()
            );
        }

        log.info(
                "Instance Selected {} -> {}",
                instance.getInstanceId(),
                instance.getServiceUrl()
        );

        exchange.getAttributes().put(
                ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR,
                newUri
        );

        log.info(
                "FINAL TARGET URI = {}",
                newUri
        );

        return chain.filter(exchange)
                .doOnSuccess(v ->
                        log.info("DOWNSTREAM SUCCESS"))
                .doOnError(e ->
                        log.error(
                                "DOWNSTREAM ERROR",
                                e
                        ));
    }

    @Override
    public int getOrder() {
        return 10150;
    }
}