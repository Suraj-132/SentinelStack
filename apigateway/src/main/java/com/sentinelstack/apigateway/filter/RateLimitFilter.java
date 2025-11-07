package com.sentinelstack.apigateway.filter;

import com.sentinelstack.apigateway.entity.User;
import com.sentinelstack.apigateway.repository.UserRepository;
import com.sentinelstack.apigateway.service.RateLimitService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;
    private final UserRepository userRepository;

    public RateLimitFilter(RateLimitService rateLimitService, UserRepository userRepository) {
        this.rateLimitService = rateLimitService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        // Only check rate limits for authenticated users
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            User user = userRepository.findByUsername(auth.getName()).orElse(null);
            
            if (user != null && rateLimitService.isRateLimitExceeded(user.getId())) {
                response.setStatus(429); // 429 Too Many Requests
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Rate limit exceeded\", \"message\": \"Too many requests. Please try again later.\"}");
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Don't apply rate limiting to login and registration endpoints
        return path.startsWith("/api/users/login") || 
               path.startsWith("/api/users/register") ||
               path.startsWith("/actuator") || 
               path.startsWith("/swagger") || 
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/error");
    }
}
