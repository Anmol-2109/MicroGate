package com.Anmol.Gateway.RateLimit.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenBucket {

    private long tokens;

    private long lastRefillTime;
}