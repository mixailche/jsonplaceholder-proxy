package mixailche.jsonplaceholder.proxy.common.repository;

import mixailche.jsonplaceholder.proxy.jooq.tables.records.RolesRecord;

import java.util.Optional;

public interface RoleRepository {

    Optional<RolesRecord> findByName(String name);

}
