package mixailche.jsonplaceholder.proxy.core.repository;

import mixailche.jsonplaceholder.proxy.common.repository.EventRepository;
import mixailche.jsonplaceholder.proxy.jooq.tables.records.EventsRecord;
import mixailche.jsonplaceholder.proxy.jooq.tables.Events;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class EventRepositoryImpl extends BasicRepositoryImpl implements EventRepository {

    public EventRepositoryImpl(DSLContext dsl) {
        super(dsl);
    }

    @Override
    public void save(EventsRecord event) {
        dsl.insertInto(Events.EVENTS).set(event).execute();
    }

}
