package com.Anmol.Gateway.Filter;

import com.Anmol.Gateway.Tracing.RequestTraceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ResponseTraceFilter
        implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {

                    RequestTraceContext context =
                            exchange.getAttribute(
                                    "traceContext");

                    if (context == null) {
                        return;
                    }

                    long duration =
                            System.currentTimeMillis()
                                    - context.getStartTime();

                    log.info("Request Completed");

                    log.info(
                            "Route={} Service={} Instance={} Duration={}ms",
                            context.getRouteId(),
                            context.getServiceName(),
                            context.getSelectedInstance(),
                            duration
                    );
                }));
    }
}