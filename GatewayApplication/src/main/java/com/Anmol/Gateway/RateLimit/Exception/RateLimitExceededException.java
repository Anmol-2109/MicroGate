package com.Anmol.Gateway.RateLimit.Exception;



public class RateLimitExceededException
        extends RuntimeException {

    public RateLimitExceededException() {

        super("Rate limit exceeded");
    }
}