package mixailche.jsonplaceholder.proxy.common.service.business;

import mixailche.jsonplaceholder.proxy.common.data.ContentType;
import mixailche.jsonplaceholder.proxy.common.data.ProxyRequestDto;
import org.springframework.http.ResponseEntity;

public interface ContentAccessService {

    ResponseEntity<?> executeProxyRequest(String jwt, ContentType contentType, ProxyRequestDto request);

}
