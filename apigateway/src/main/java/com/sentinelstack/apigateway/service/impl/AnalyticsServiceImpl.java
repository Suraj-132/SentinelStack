package com.sentinelstack.apigateway.service.impl;

import com.sentinelstack.apigateway.entity.APIRequest;
import com.sentinelstack.apigateway.repository.APIRequestRepository;
import com.sentinelstack.apigateway.service.AnalyticsService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final APIRequestRepository apiRequestRepository;

    public AnalyticsServiceImpl(APIRequestRepository apiRequestRepository) {
        this.apiRequestRepository = apiRequestRepository;
    }

    @Override
    public long getTotalRequestCount(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return apiRequestRepository.countByUserIdAndTimestampBetween(userId, startDate, endDate);
    }

    @Override
    public Double getAverageResponseTime(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return apiRequestRepository.averageResponseTimeByUserIdAndTimestampBetween(userId, startDate, endDate);
    }

    @Override
    public Map<Integer, Long> getRequestCountByStatus(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<APIRequest> requests = apiRequestRepository.findByUserIdAndTimestampBetween(userId, startDate, endDate);
        return requests.stream()
                .collect(Collectors.groupingBy(APIRequest::getStatusCode, Collectors.counting()));
    }

    @Override
    public Map<String, Long> getRequestCountByEndpoint(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<APIRequest> requests = apiRequestRepository.findByUserIdAndTimestampBetween(userId, startDate, endDate);
        return requests.stream()
                .collect(Collectors.groupingBy(APIRequest::getPath, Collectors.counting()));
    }

    @Override
    public List<APIRequest> getRecentRequests(Long userId, int limit) {
        return apiRequestRepository.findByUserIdOrderByTimestampDesc(userId, PageRequest.of(0, limit));
    }

    @Override
    public Map<String, Object> getAnalyticsSummary(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> summary = new HashMap<>();
        
        long totalRequests = getTotalRequestCount(userId, startDate, endDate);
        Double avgResponseTime = getAverageResponseTime(userId, startDate, endDate);
        Map<Integer, Long> statusDistribution = getRequestCountByStatus(userId, startDate, endDate);
        Map<String, Long> endpointUsage = getRequestCountByEndpoint(userId, startDate, endDate);
        
        // Calculate success rate
        long successCount = statusDistribution.entrySet().stream()
                .filter(entry -> entry.getKey() >= 200 && entry.getKey() < 300)
                .mapToLong(Map.Entry::getValue)
                .sum();
        double successRate = totalRequests > 0 ? (successCount * 100.0 / totalRequests) : 0;
        
        // Calculate error rate
        long errorCount = statusDistribution.entrySet().stream()
                .filter(entry -> entry.getKey() >= 400)
                .mapToLong(Map.Entry::getValue)
                .sum();
        double errorRate = totalRequests > 0 ? (errorCount * 100.0 / totalRequests) : 0;
        
        summary.put("totalRequests", totalRequests);
        summary.put("averageResponseTime", avgResponseTime != null ? avgResponseTime : 0);
        summary.put("successRate", successRate);
        summary.put("errorRate", errorRate);
        summary.put("statusDistribution", statusDistribution);
        summary.put("endpointUsage", endpointUsage);
        summary.put("startDate", startDate);
        summary.put("endDate", endDate);
        
        return summary;
    }
}
