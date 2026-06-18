package com.Anmol.Gateway.Security;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityWebFilterChain
    securityWebFilterChain(
            ServerHttpSecurity http
    ) {
        log.info("JWT FILTER EXECUTED");



        return http

                .csrf(
                        ServerHttpSecurity
                                .CsrfSpec::disable
                )

                .addFilterAt(
                        jwtFilter,
                        SecurityWebFiltersOrder.AUTHENTICATION
                )




                .authorizeExchange(exchange ->
                        exchange

                                .pathMatchers(
                                        "/user/auth/**" , "/auth/**" ,     "/actuator/**"

                                )
                                .permitAll()

                                .pathMatchers(

                                        "/services/heartbeat",
                                        "/services/discover/**",

                                        "/swagger-ui/**",
                                        "/swagger-ui.html",

                                        "/swagger-config",
                                        "/swagger/services",

                                        "/v3/api-docs/**"
                                )
                                .permitAll()

                                .anyExchange()
                                .authenticated()

                )


                .build();
    }
}