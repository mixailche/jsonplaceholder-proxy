package mixailche.jsonplaceholder.proxy.common.repository;

import mixailche.jsonplaceholder.proxy.jooq.tables.records.EventsRecord;

public interface EventRepository {

    void save(EventsRecord event);

}
