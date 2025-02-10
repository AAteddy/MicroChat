
package com.tedsaasfaha.MicroChat.config;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitingConfig {

    // Limit: 3 password reset requests per hour per Ip
    @Bean(name = "ipRateLimiter")
    public Bucket ipRateLimiter() {
        Bandwidth limit = Bandwidth.builder()
                .capacity(3) // 3 requests
                .refillGreedy(3, Duration.ofHours(1)) // Refill 3 tokens every hour
                .build();
        return Bucket.builder().addLimit(limit).build();
    }

    // Limit: 5 password reset requests per hour per user
    @Bean(name = "userRateLimiter")
    public Bucket userRateLimiter() {
        Bandwidth limit = Bandwidth.builder()
                .capacity(5)
                .refillGreedy(5, Duration.ofHours(1))
                .build();
        return Bucket.builder().addLimit(limit).build();
    }
}
//