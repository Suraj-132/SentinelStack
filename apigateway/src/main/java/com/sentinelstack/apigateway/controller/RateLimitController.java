package com.sentinelstack.apigateway.controller;

import com.sentinelstack.apigateway.entity.RateLimit;
import com.sentinelstack.apigateway.entity.User;
import com.sentinelstack.apigateway.service.RateLimitService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rate-limits")
public class RateLimitController {

    private final RateLimitService rateLimitService;

    public RateLimitController(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @GetMapping
    public ResponseEntity<RateLimit> getRateLimit(@AuthenticationPrincipal User user) {
        RateLimit rateLimit = rateLimitService.getRateLimitByUserId(user.getId())
                .orElse(rateLimitService.getDefaultRateLimit());
        return ResponseEntity.ok(rateLimit);
    }

    @PutMapping
    public ResponseEntity<RateLimit> updateRateLimit(
            @AuthenticationPrincipal User user,
            @RequestBody RateLimit rateLimitRequest) {
        
        rateLimitRequest.setUserId(user.getId());
        RateLimit savedRateLimit = rateLimitService.saveRateLimit(rateLimitRequest);
        return ResponseEntity.ok(savedRateLimit);
    }

    @GetMapping("/default")
    public ResponseEntity<RateLimit> getDefaultRateLimit() {
        return ResponseEntity.ok(rateLimitService.getDefaultRateLimit());
    }
}
