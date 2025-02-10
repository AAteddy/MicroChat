package com.tedsaasfaha.MicroChat.filter;


import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1) // Run before other filters
public class RateLimitingFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("ipRateLimiter")
    private Bucket ipRateLimiter;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Apply rate limiting only to /forgot-password endpoint
        if (request.getRequestURI().equals("/api/auth/forgot-password")) {
            String clientIp = request.getRemoteAddr();


            // Try to consume a token from the bucket
            if (ipRateLimiter.tryConsume(1)) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write(
                        "Too many password reset requests. Please try again later.");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
