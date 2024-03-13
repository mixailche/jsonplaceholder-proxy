package com.jsonplaceholder.proxy.common.service.business;

import com.jsonplaceholder.proxy.common.data.ContentType;
import com.jsonplaceholder.proxy.common.data.ProxyRequestDto;
import org.springframework.http.ResponseEntity;

public interface ContentAccessService {

    ResponseEntity<?> executeProxyRequest(String jwt, ContentType contentType, ProxyRequestDto request);

}
