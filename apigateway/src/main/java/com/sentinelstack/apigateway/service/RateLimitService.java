package com.sentinelstack.apigateway.service;

import com.sentinelstack.apigateway.entity.RateLimit;

import java.util.Optional;

public interface RateLimitService {
    
    /**
     * Check if user has exceeded their rate limit
     */
    boolean isRateLimitExceeded(Long userId);
    
    /**
     * Get rate limit configuration for a user
     */
    Optional<RateLimit> getRateLimitByUserId(Long userId);
    
    /**
     * Create or update rate limit for a user
     */
    RateLimit saveRateLimit(RateLimit rateLimit);
    
    /**
     * Get default rate limit values
     */
    RateLimit getDefaultRateLimit();
}
