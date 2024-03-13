package com.jsonplaceholder.proxy.core.repository;

import com.jsonplaceholder.proxy.common.repository.EventRepository;
import com.jsonplaceholder.proxy.jooq.tables.records.EventsRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.jsonplaceholder.proxy.jooq.tables.Events.EVENTS;

@Repository
public class EventRepositoryImpl extends BasicRepositoryImpl implements EventRepository {

    public EventRepositoryImpl(DSLContext dsl) {
        super(dsl);
    }

    @Override
    public void save(EventsRecord event) {
        dsl.insertInto(EVENTS).set(event).execute();
    }

}
