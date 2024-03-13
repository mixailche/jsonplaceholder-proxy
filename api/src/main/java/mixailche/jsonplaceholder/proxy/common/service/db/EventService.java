package mixailche.jsonplaceholder.proxy.common.service.db;

import mixailche.jsonplaceholder.proxy.common.data.ProxyRequestDto;

public interface EventService {

    void saveProcessedProxyRequest(ProxyRequestDto request);

}
