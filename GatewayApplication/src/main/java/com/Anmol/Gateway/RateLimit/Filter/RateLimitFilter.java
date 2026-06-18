package com.Anmol.Gateway.RateLimit.Filter;



import com.Anmol.Gateway.Metrics.GatewayMetrics;
import com.Anmol.Gateway.RateLimit.Service.TokenBucketService;
import com.Anmol.Gateway.Security.JwtPrincipal;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(2)
@RequiredArgsConstructor
@Slf4j
public class RateLimitFilter
        implements WebFilter {

    private final TokenBucketService tokenBucketService;
    private final MeterRegistry meterRegistry;

//    @Override
//    public Mono<Void> filter(
//            ServerWebExchange exchange,
//            WebFilterChain chain
//    ) {
//
//        return ReactiveSecurityContextHolder
//                .getContext()
//                .map(context ->
//                        context.getAuthentication()
//                )
//                .defaultIfEmpty(null)
//                .flatMap(authentication -> {
//
//                    if(authentication == null)
//                    {
//                        return chain.filter(exchange);
//                    }
//
//                    JwtPrincipal principal =
//                            (JwtPrincipal)
//                                    authentication.getPrincipal();
//
//                    String route =
//                            exchange.getRequest()
//                                    .getURI()
//                                    .getPath();
//
//                    String key =
//                            "rate:user:"
//                                    + principal.getUserId()
//                                    + ":"
//                                    + route;
//
//                    boolean allowed =
//                            tokenBucketService
//                                    .allowRequest(key);
//
//                    if(!allowed)
//                    {
//
//                        log.warn(
//                                "Rate Limit Rejected userId={} route={}",
//                                principal.getUserId(),
//                                route
//                        );
//
//                        exchange.getResponse()
//                                .setStatusCode(
//                                        HttpStatus.TOO_MANY_REQUESTS
//                                );
//
//                        return exchange.getResponse()
//                                .setComplete();
//                    }
//
//                    log.info(
//                            "Rate Limit Passed userId={} route={}",
//                            principal.getUserId(),
//                            route
//                    );
//
//                    return chain.filter(exchange);
//                });
//    }

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            WebFilterChain chain
    ) {

        String route =
                exchange.getRequest()
                        .getURI()
                        .getPath();

        if (
                route.startsWith("/services/heartbeat")
                        || route.startsWith("/services/register")
                        || route.startsWith("/services/discover")
                        || route.startsWith("/actuator")
                        || route.startsWith("/fallback")
                        || route.startsWith("/routes")
        )
        {
            return chain.filter(exchange);
        }


        String clientIp =
                exchange.getRequest()
                        .getRemoteAddress() != null
                        ? exchange.getRequest()
                        .getRemoteAddress()
                        .getAddress()
                        .getHostAddress()
                        : "unknown";

        String key =
                "rate:ip:"
                        + clientIp
                        + ":"
                        + route;

        boolean allowed =
                tokenBucketService.allowRequest(key);

        if (!allowed) {

            log.warn(
                    "Rate Limit Rejected ip={} route={}",
                    clientIp,
                    route
            );

            exchange.getResponse()
                    .setStatusCode(
                            HttpStatus.TOO_MANY_REQUESTS
                    );

            meterRegistry.counter(
                    GatewayMetrics.RATE_LIMIT_REJECTED
            ).increment();

            return exchange.getResponse()
                    .setComplete();
        }

        log.info(
                "Rate Limit Passed ip={} route={}",
                clientIp,
                route
        );

        return chain.filter(exchange);
    }

}