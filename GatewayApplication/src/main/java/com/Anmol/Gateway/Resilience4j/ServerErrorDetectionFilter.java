package com.Anmol.Gateway.Resilience4j;



import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ServerErrorDetectionFilter
        implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain
    ) {

        return chain.filter(exchange)
                .then(
                        Mono.defer(() -> {

                            HttpStatusCode status =
                                    exchange.getResponse()
                                            .getStatusCode();

                            log.info(
                                    "Gateway Response Status = {}",
                                    status
                            );

                            if (
                                    status != null &&
                                            status.is5xxServerError()
                            ) {

                                log.error(
                                        "5xx detected {}",
                                        status.value()
                                );

                                throw new RuntimeException(
                                        "Downstream service failure"
                                );
                            }

                            return Mono.empty();
                        })
                );
    }

    @Override
    public int getOrder() {
        return -1;
    }
}