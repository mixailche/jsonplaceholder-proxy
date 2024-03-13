package com.jsonplaceholder.proxy.core.service.business;

import com.jsonplaceholder.proxy.common.data.ProxyRequestDto;
import com.jsonplaceholder.proxy.common.data.SupportedHttpMethod;
import com.jsonplaceholder.proxy.common.service.business.CachingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProxyRequestCachingService implements CachingService<ProxyRequestDto, ResponseEntity<?>> {

    private final Map<String, Map<SupportedHttpMethod, ResponseEntity<?>>> cache = new ConcurrentHashMap<>();

    @Override
    public Optional<ResponseEntity<?>> get(ProxyRequestDto request) {
        return Optional.ofNullable(cache.get(request.getContextPath()))
                .map(subCache -> subCache.get(request.getMethod()));
    }

    @Override
    public void update(ProxyRequestDto request, ResponseEntity<?> response) {
        SupportedHttpMethod method = request.getMethod();
        Map<SupportedHttpMethod, ResponseEntity<?>> subCache = cache.computeIfAbsent(
                request.getContextPath(), ignored -> new ConcurrentHashMap<>()
        );
        if (!method.isSafe()) {
            subCache.clear();
        }
        if (method.isIdempotent()) {
            subCache.put(method, response);
        }
    }

}
