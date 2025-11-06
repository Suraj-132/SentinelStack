package com.sentinelstack.apigateway.service;

import com.sentinelstack.apigateway.entity.APIRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AnalyticsService {
    
    /**
     * Get total request count for a user within a time period
     */
    long getTotalRequestCount(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get average response time for a user within a time period
     */
    Double getAverageResponseTime(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get request count by status code for a user within a time period
     */
    Map<Integer, Long> getRequestCountByStatus(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get request count by endpoint for a user within a time period
     */
    Map<String, Long> getRequestCountByEndpoint(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get recent API requests for a user
     */
    List<APIRequest> getRecentRequests(Long userId, int limit);
    
    /**
     * Get analytics summary for a user
     */
    Map<String, Object> getAnalyticsSummary(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
