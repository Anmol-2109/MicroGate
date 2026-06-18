package com.Anmol.Gateway.Metrics;

public final class GatewayMetrics {

    public static final String REQUESTS_TOTAL =
            "gateway_requests_total";

    public static final String RATE_LIMIT_REJECTED =
            "gateway_ratelimit_rejected";

    public static final String JWT_FAILURES =
            "gateway_jwt_failures";

    public static final String CIRCUIT_BREAKER_OPEN =
            "gateway_circuitbreaker_open";
}