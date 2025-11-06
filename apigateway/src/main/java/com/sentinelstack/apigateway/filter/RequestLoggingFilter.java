package com.sentinelstack.apigateway.filter;

import com.sentinelstack.apigateway.entity.APIRequest;
import com.sentinelstack.apigateway.entity.User;
import com.sentinelstack.apigateway.repository.APIRequestRepository;
import com.sentinelstack.apigateway.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final APIRequestRepository apiRequestRepository;
    private final UserRepository userRepository;

    public RequestLoggingFilter(APIRequestRepository apiRequestRepository, UserRepository userRepository) {
        this.apiRequestRepository = apiRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        long startTime = System.currentTimeMillis();
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        
        try {
            filterChain.doFilter(request, responseWrapper);
        } finally {
            long responseTime = System.currentTimeMillis() - startTime;
            logRequest(request, responseWrapper, responseTime);
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequest(HttpServletRequest request, ContentCachingResponseWrapper response, long responseTime) {
        try {
            // Skip logging for actuator and internal endpoints
            String path = request.getRequestURI();
            if (path.startsWith("/actuator") || path.startsWith("/error")) {
                return;
            }

            APIRequest apiRequest = new APIRequest();
            apiRequest.setMethod(request.getMethod());
            apiRequest.setPath(path);
            apiRequest.setStatusCode(response.getStatus());
            apiRequest.setResponseTimeMs(responseTime);
            apiRequest.setIpAddress(getClientIP(request));
            apiRequest.setUserAgent(request.getHeader("User-Agent"));
            
            // Get user ID if authenticated
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
                userRepository.findByUsername(auth.getName()).ifPresent(user -> 
                    apiRequest.setUserId(user.getId())
                );
            }
            
            // Set request/response sizes
            apiRequest.setRequestSizeBytes((long) request.getContentLength());
            apiRequest.setResponseSizeBytes((long) response.getContentSize());
            
            // Save asynchronously to avoid blocking
            apiRequestRepository.save(apiRequest);
        } catch (Exception e) {
            // Log error but don't fail the request
            logger.error("Failed to log API request", e);
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator") || path.startsWith("/swagger") || path.startsWith("/v3/api-docs");
    }
}
