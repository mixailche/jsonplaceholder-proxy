package mixailche.jsonplaceholder.proxy.core.repository;

import mixailche.jsonplaceholder.proxy.common.repository.RoleRepository;
import mixailche.jsonplaceholder.proxy.jooq.tables.records.RolesRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static mixailche.jsonplaceholder.proxy.jooq.tables.Roles.ROLES;

@Repository
public class RoleRepositoryImpl extends BasicRepositoryImpl implements RoleRepository {

    public RoleRepositoryImpl(DSLContext dsl) {
        super(dsl);
    }

    @Override
    public Optional<RolesRecord> findByName(String name) {
        return dsl.selectFrom(ROLES)
                .where(ROLES.NAME.eq(name))
                .stream().findAny();
    }

}
