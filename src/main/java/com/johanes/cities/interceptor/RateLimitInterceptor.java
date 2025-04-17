package com.johanes.cities.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class RateLimitInterceptor implements HandlerInterceptor {
    private static final int MAX_REQUESTS = 100;
    private static final long TIME_WINDOW = TimeUnit.MINUTES.toMillis(1);
    private final Map<String, RequestCounter> requestCounters = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String clientIp = request.getRemoteAddr();
        RequestCounter counter = requestCounters.computeIfAbsent(clientIp, k -> new RequestCounter());

        if (counter.isRateLimited()) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return false;
        }

        counter.increment();
        return true;
    }

    private static class RequestCounter {
        private int count = 0;
        private long windowStart = System.currentTimeMillis();

        synchronized boolean isRateLimited() {
            long now = System.currentTimeMillis();
            if (now - windowStart > TIME_WINDOW) {
                count = 0;
                windowStart = now;
            }
            return count >= MAX_REQUESTS;
        }

        synchronized void increment() {
            count++;
        }
    }
}