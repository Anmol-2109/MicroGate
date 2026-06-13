package com.Anmol.Gateway.RateLimit.Config;


import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class RateLimitProperties {

    private final long capacity = 100;

    private final long refillTokens = 20;

    private final long refillPeriodSeconds = 1;}