package com.jsonplaceholder.proxy.core.repository;

import com.jsonplaceholder.proxy.common.repository.UserRoleRepository;
import com.jsonplaceholder.proxy.jooq.tables.records.RolesRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.jsonplaceholder.proxy.jooq.tables.Roles.ROLES;
import static com.jsonplaceholder.proxy.jooq.tables.UserRoles.USER_ROLES;

@Repository
public class UserRoleRepositoryImpl extends BasicRepositoryImpl implements UserRoleRepository {

    public UserRoleRepositoryImpl(DSLContext dsl) {
        super(dsl);
    }

    @Override
    public void insert(long userId, int roleId) {
        dsl.insertInto(USER_ROLES)
                .set(USER_ROLES.USER_ID, userId)
                .set(USER_ROLES.ROLE_ID, roleId)
                .execute();
    }

    @Override
    public List<RolesRecord> findAllRolesByUserId(long userId) {
        return dsl.select(ROLES.fields())
                .from(USER_ROLES.join(ROLES)
                        .on(USER_ROLES.ROLE_ID.eq(ROLES.ID)))
                .where(USER_ROLES.USER_ID.eq(userId))
                .fetchInto(ROLES);
    }

}
