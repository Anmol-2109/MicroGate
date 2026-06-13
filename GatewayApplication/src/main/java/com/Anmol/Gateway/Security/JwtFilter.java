package com.Anmol.Gateway.Security;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter implements WebFilter {

    private final SecurityUtil securityUtil;

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            WebFilterChain chain
    ) {

        System.out.println("=====================================================================================================");

        String authHeader =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);

        System.out.println(
                "URI is printed to check ---------------= " +
                        exchange.getRequest().getURI()
        );

        System.out.println(
                "AUTH HEADER = " +
                        exchange.getRequest()
                                .getHeaders()
                                .getFirst(HttpHeaders.AUTHORIZATION)
        );

        if(authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            return chain.filter(exchange);
        }

        try {

            String token =
                    authHeader.substring(7);

            String username =
                    securityUtil
                            .getUsernameFromToken(token);

            Long userId =
                    securityUtil
                            .getUserIdFromToken(token);

            JwtPrincipal principal =
                    new JwtPrincipal(
                            userId,
                            username
                    );

            log.info(
                    "Goes inside authentication"
            );

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            Collections.emptyList()
                    );

            log.info(
                    "AUTH AUTHENTICATED = {}",
                    auth.isAuthenticated()
            );

            log.info(
                    "JWT Success userId={} username={}",
                    userId,
                    username
            );

            return chain.filter(exchange)
                    .contextWrite(
                            ReactiveSecurityContextHolder
                                    .withAuthentication(auth)
                    );

        } catch (Exception ex) {

            log.warn(
                    "Rate Limit Rejected"
            );

            exchange.getResponse()
                    .setStatusCode(
                            org.springframework.http.HttpStatus.UNAUTHORIZED
                    );

            return exchange.getResponse()
                    .setComplete();
        }
    }
}