package com.jsonplaceholder.proxy.common.service.db;

import com.jsonplaceholder.proxy.common.data.ProxyRequestDto;

public interface EventService {

    void saveProcessedProxyRequest(ProxyRequestDto request);

}
