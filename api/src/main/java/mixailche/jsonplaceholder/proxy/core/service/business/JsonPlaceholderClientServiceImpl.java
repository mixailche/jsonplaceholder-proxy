package mixailche.jsonplaceholder.proxy.core.service.business;

import mixailche.jsonplaceholder.proxy.common.data.EventResult;
import mixailche.jsonplaceholder.proxy.common.data.ProxyRequestDto;
import mixailche.jsonplaceholder.proxy.common.service.business.CachingService;
import mixailche.jsonplaceholder.proxy.common.service.business.JsonPlaceholderClientService;
import mixailche.jsonplaceholder.proxy.common.service.db.EventService;
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
        removeContentLength(request.getHeaders());
    }

    private ResponseEntity<?> mapResponse(ResponseEntity<String> response) {
        MultiValueMap<String, String> modifiedHeaders = HttpHeaders.writableHttpHeaders(response.getHeaders());
        removeContentLength(modifiedHeaders);
        return new ResponseEntity<>(response.getBody(), modifiedHeaders, response.getStatusCode());
    }

    // Content length in the target response and in the forwarded response headers
    // may differ because of encoding issues
    private void removeContentLength(MultiValueMap<String, String> headers) {
        headers.remove(HttpHeaders.CONTENT_LENGTH);
    }

    private void saveExecutionResult(ProxyRequestDto request, ResponseEntity<?> response) {
        request.setResult(response.getStatusCode().is2xxSuccessful()
                ? EventResult.SATISFIED
                : EventResult.BAD_REQUEST);
        eventService.saveProcessedProxyRequest(request);
    }

}
