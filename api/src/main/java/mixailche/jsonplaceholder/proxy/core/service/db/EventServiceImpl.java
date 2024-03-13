package mixailche.jsonplaceholder.proxy.core.service.db;

import mixailche.jsonplaceholder.proxy.common.data.ProxyRequestDto;
import mixailche.jsonplaceholder.proxy.common.repository.EventRepository;
import mixailche.jsonplaceholder.proxy.common.service.db.EventService;
import mixailche.jsonplaceholder.proxy.jooq.enums.AccessLevel;
import mixailche.jsonplaceholder.proxy.jooq.enums.EventMethod;
import mixailche.jsonplaceholder.proxy.jooq.enums.EventResult;
import mixailche.jsonplaceholder.proxy.jooq.tables.records.EventsRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    @Autowired
    private final EventRepository eventRepository;

    @Override
    public void saveProcessedProxyRequest(ProxyRequestDto request) {
        EventsRecord record = new EventsRecord();
        record.setUserId(request.getUserId());
        record.setMethod(EventMethod.valueOf(request.getMethod().name()));
        record.setQueryUrl(cutQueryString(request.getContextPath()));
        record.setRequiredAccess(AccessLevel.valueOf(request.getRequiredAccess().name()));
        record.setUserId(request.getUserId());
        record.setResult(EventResult.valueOf(request.getResult().name()));
        eventRepository.save(record);
    }

    private static final int MAX_QUERY_LENGTH = 200;

    private String cutQueryString(String query) {
        int len = Integer.min(query.length(), MAX_QUERY_LENGTH);
        return query.substring(0, len);
    }

}
