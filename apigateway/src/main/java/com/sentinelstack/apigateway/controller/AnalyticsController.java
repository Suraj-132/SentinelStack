package com.sentinelstack.apigateway.controller;

import com.sentinelstack.apigateway.entity.APIRequest;
import com.sentinelstack.apigateway.entity.User;
import com.sentinelstack.apigateway.service.AnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getAnalyticsSummary(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        // Default to last 30 days if not specified
        if (startDate == null) {
            startDate = LocalDateTime.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }
        
        Map<String, Object> summary = analyticsService.getAnalyticsSummary(user.getId(), startDate, endDate);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/requests/count")
    public ResponseEntity<Long> getRequestCount(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        if (startDate == null) {
            startDate = LocalDateTime.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }
        
        long count = analyticsService.getTotalRequestCount(user.getId(), startDate, endDate);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/requests/response-time")
    public ResponseEntity<Double> getAverageResponseTime(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        if (startDate == null) {
            startDate = LocalDateTime.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }
        
        Double avgTime = analyticsService.getAverageResponseTime(user.getId(), startDate, endDate);
        return ResponseEntity.ok(avgTime != null ? avgTime : 0.0);
    }

    @GetMapping("/requests/by-status")
    public ResponseEntity<Map<Integer, Long>> getRequestsByStatus(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        if (startDate == null) {
            startDate = LocalDateTime.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }
        
        Map<Integer, Long> distribution = analyticsService.getRequestCountByStatus(user.getId(), startDate, endDate);
        return ResponseEntity.ok(distribution);
    }

    @GetMapping("/requests/by-endpoint")
    public ResponseEntity<Map<String, Long>> getRequestsByEndpoint(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        if (startDate == null) {
            startDate = LocalDateTime.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }
        
        Map<String, Long> distribution = analyticsService.getRequestCountByEndpoint(user.getId(), startDate, endDate);
        return ResponseEntity.ok(distribution);
    }

    @GetMapping("/requests/recent")
    public ResponseEntity<List<APIRequest>> getRecentRequests(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "50") int limit) {
        
        if (limit > 500) {
            limit = 500; // Cap at 500
        }
        
        List<APIRequest> requests = analyticsService.getRecentRequests(user.getId(), limit);
        return ResponseEntity.ok(requests);
    }
}
