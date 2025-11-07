package com.sentinelstack.apigateway.controller;

import com.sentinelstack.apigateway.entity.User;
import com.sentinelstack.apigateway.repository.APIKeyRepository;
import com.sentinelstack.apigateway.repository.APIRequestRepository;
import com.sentinelstack.apigateway.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final APIRequestRepository apiRequestRepository;
    private final APIKeyRepository apiKeyRepository;

    public AdminController(UserRepository userRepository, 
                          APIRequestRepository apiRequestRepository,
                          APIKeyRepository apiKeyRepository) {
        this.userRepository = userRepository;
        this.apiRequestRepository = apiRequestRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(@AuthenticationPrincipal User user) {
        // Only allow ADMIN role
        if (!"ADMIN".equals(user.getRole())) {
            return ResponseEntity.status(403).build();
        }

        Map<String, Object> dashboard = new HashMap<>();
        
        // User statistics
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.findAll().stream()
                .filter(u -> u.getLastLoginAt() != null && 
                       u.getLastLoginAt().isAfter(LocalDateTime.now().minusDays(7)))
                .count();
        
        // API statistics
        long totalRequests = apiRequestRepository.count();
        long totalApiKeys = apiKeyRepository.count();
        long activeApiKeys = apiKeyRepository.countByIsActive(true);
        
        // Recent activity - count all requests in last 24 hours
        LocalDateTime last24Hours = LocalDateTime.now().minusHours(24);
        long requestsLast24h = apiRequestRepository.findAll().stream()
                .filter(r -> r.getTimestamp().isAfter(last24Hours))
                .count();
        
        dashboard.put("totalUsers", totalUsers);
        dashboard.put("activeUsers", activeUsers);
        dashboard.put("totalRequests", totalRequests);
        dashboard.put("requestsLast24h", requestsLast24h);
        dashboard.put("totalApiKeys", totalApiKeys);
        dashboard.put("activeApiKeys", activeApiKeys);
        dashboard.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@AuthenticationPrincipal User user) {
        if (!"ADMIN".equals(user.getRole())) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserById(@AuthenticationPrincipal User user, @PathVariable Long userId) {
        if (!"ADMIN".equals(user.getRole())) {
            return ResponseEntity.status(403).build();
        }
        
        return userRepository.findById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal User user, @PathVariable Long userId) {
        if (!"ADMIN".equals(user.getRole())) {
            return ResponseEntity.status(403).build();
        }
        
        if (userId.equals(user.getId())) {
            return ResponseEntity.badRequest().build(); // Can't delete yourself
        }
        
        userRepository.deleteById(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> getHealth() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(health);
    }
}
