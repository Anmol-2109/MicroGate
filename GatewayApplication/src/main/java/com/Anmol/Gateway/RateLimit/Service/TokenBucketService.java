package com.Anmol.Gateway.RateLimit.Service;



import com.Anmol.Gateway.RateLimit.Config.RateLimitProperties;
import com.Anmol.Gateway.RateLimit.Model.TokenBucket;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenBucketService {

    private final StringRedisTemplate redisTemplate;

    private final ObjectMapper mapper;

    private final RateLimitProperties properties;

    public boolean allowRequest(
            String key
    ) {

        try {

            String value =
                    redisTemplate.opsForValue().get(key);

            TokenBucket bucket;

            if(value == null)
            {
                bucket =
                        new TokenBucket(
                                properties.getCapacity(),
                                System.currentTimeMillis()
                        );
            }
            else
            {
                bucket =
                        mapper.readValue(
                                value,
                                TokenBucket.class
                        );
            }

            refill(bucket);

            if(bucket.getTokens() <= 0)
            {
                save(key,bucket);
                return false;
            }

            bucket.setTokens(
                    bucket.getTokens() - 1
            );

            save(key,bucket);

            return true;

        } catch (Exception ex) {

            throw new RuntimeException(ex);
        }
    }

    private void refill(
            TokenBucket bucket
    ) {

        long now =
                System.currentTimeMillis();

        long elapsed =
                now -
                        bucket.getLastRefillTime();

        long tokensToAdd =
                (elapsed /
                        (properties.getRefillPeriodSeconds() * 1000))
                        * properties.getRefillTokens();

        if(tokensToAdd > 0)
        {
            bucket.setTokens(
                    Math.min(
                            properties.getCapacity(),
                            bucket.getTokens()
                                    + tokensToAdd
                    )
            );

            bucket.setLastRefillTime(now);
        }
    }

    private void save(
            String key,
            TokenBucket bucket
    ) throws Exception {

        redisTemplate.opsForValue().set(
                key,
                mapper.writeValueAsString(bucket),
                Duration.ofMinutes(10)
        );
    }
}