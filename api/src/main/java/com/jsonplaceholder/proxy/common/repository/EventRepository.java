package com.jsonplaceholder.proxy.common.repository;

import com.jsonplaceholder.proxy.jooq.tables.records.EventsRecord;

public interface EventRepository {

    void save(EventsRecord event);

}
