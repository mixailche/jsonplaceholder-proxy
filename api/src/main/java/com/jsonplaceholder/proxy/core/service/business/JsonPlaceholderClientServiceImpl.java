package com.jsonplaceholder.proxy.core.service.business;

import com.jsonplaceholder.proxy.common.data.EventResult;
import com.jsonplaceholder.proxy.common.data.ProxyRequestDto;
import com.jsonplaceholder.proxy.common.service.business.CachingService;
import com.jsonplaceholder.proxy.common.service.business.JsonPlaceholderClientService;
import com.jsonplaceholder.proxy.common.service.db.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JsonPlaceholderClientServiceImpl implements JsonPlaceholderClientService {

    @Autowired
    private final EventService eventService;

    @Autowired
    private final URI targetUri;

    @Autowired
    private final CachingService<ProxyRequestDto, ResponseEntity<?>> cachingService;

    private RestTemplate client;

    @Autowired
    private void setupClient() {
        this.client = new RestTemplate();
        this.client.setErrorHandler(new ForwardingErrorHandler());
    }

    private static class ForwardingErrorHandler extends DefaultResponseErrorHandler {

        @Override
        protected boolean hasError(HttpStatusCode statusCode) {
            return statusCode.is5xxServerError();
        }

    }

    @Override
    public ResponseEntity<?> executeRequest(ProxyRequestDto request) {
        return cachingService.get(request)
                .orElseGet(() -> executeRequestImpl(request));
    }

    private ResponseEntity<?> executeRequestImpl(ProxyRequestDto request) {
        prepareRequest(request);
        ResponseEntity<?> response = mapResponse(
                client.exchange(
                        targetUri.resolve(request.getContextPath()),
                        HttpMethod.valueOf(request.getMethod().name()),
                        new HttpEntity<>(request.getBody(), request.getHeaders()),
                        String.class
                )
        );
        cachingService.update(request, response);
        saveExecutionResult(request, response);
        return response;
    }

    private void prepareRequest(ProxyRequestDto request) {
        request.getHeaders().set(HttpHeaders.ACCEPT_ENCODING, "identity");
        setContentLength(request.getHeaders(), request.getBody());
    }

    private ResponseEntity<?> mapResponse(ResponseEntity<String> response) {
        MultiValueMap<String, String> modifiedHeaders = HttpHeaders.writableHttpHeaders(response.getHeaders());
        setContentLength(modifiedHeaders, response.getBody());
        return new ResponseEntity<>(response.getBody(), modifiedHeaders, response.getStatusCode());
    }

    private void setContentLength(MultiValueMap<String, String> headers, String body) {
        headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(Objects.requireNonNullElse(body, "").length()));
    }

    private void saveExecutionResult(ProxyRequestDto request, ResponseEntity<?> response) {
        request.setResult(response.getStatusCode().is2xxSuccessful()
                ? EventResult.SATISFIED
                : EventResult.BAD_REQUEST);
        eventService.saveProcessedProxyRequest(request);
    }

}
