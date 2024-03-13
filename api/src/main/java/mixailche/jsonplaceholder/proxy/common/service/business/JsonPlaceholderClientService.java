package mixailche.jsonplaceholder.proxy.common.service.business;

import mixailche.jsonplaceholder.proxy.common.data.ProxyRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

public interface JsonPlaceholderClientService {

    ResponseEntity<?> executeRequest(ProxyRequestDto request) throws RestClientException;

}
