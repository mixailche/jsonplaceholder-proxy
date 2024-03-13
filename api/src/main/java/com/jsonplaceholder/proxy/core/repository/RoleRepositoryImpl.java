package com.jsonplaceholder.proxy.core.repository;

import com.jsonplaceholder.proxy.common.repository.RoleRepository;
import com.jsonplaceholder.proxy.jooq.tables.records.RolesRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.jsonplaceholder.proxy.jooq.tables.Roles.ROLES;

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
