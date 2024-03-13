package com.jsonplaceholder.proxy.common.data;

import lombok.Builder;
import lombok.Data;
import org.springframework.util.MultiValueMap;

@Data
@Builder
public class ProxyRequestDto {
    private SupportedHttpMethod method;
    private MultiValueMap<String, String> headers;
    private String contextPath;
    private String body;
    private AccessLevel requiredAccess;
    private EventResult result;
    private long userId;
}
