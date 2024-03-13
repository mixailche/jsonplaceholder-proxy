package com.jsonplaceholder.proxy.common.service.business;

import com.jsonplaceholder.proxy.common.data.ProxyRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

public interface JsonPlaceholderClientService {

    ResponseEntity<?> executeRequest(ProxyRequestDto request) throws RestClientException;

}
