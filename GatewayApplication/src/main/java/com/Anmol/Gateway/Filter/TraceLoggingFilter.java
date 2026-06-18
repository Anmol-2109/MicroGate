package com.Anmol.Gateway.Filter;

import com.Anmol.Gateway.Metrics.GatewayMetrics;
import com.Anmol.Gateway.Tracing.RequestTraceContext;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class TraceLoggingFilter implements GlobalFilter {

    private final MeterRegistry meterRegistry;

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        RequestTraceContext context =
                new RequestTraceContext();

        context.setStartTime(
                System.currentTimeMillis());

        exchange.getAttributes()
                .put("traceContext", context);

        log.info(
                "Request Started {} {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI()
        );

        meterRegistry.counter(
                GatewayMetrics.REQUESTS_TOTAL
        ).increment();

        return chain.filter(exchange);
    }
}