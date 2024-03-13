package mixailche.jsonplaceholder.proxy.core.repository;

import mixailche.jsonplaceholder.proxy.common.repository.UserRoleRepository;
import mixailche.jsonplaceholder.proxy.jooq.tables.records.RolesRecord;
import mixailche.jsonplaceholder.proxy.jooq.tables.Roles;
import mixailche.jsonplaceholder.proxy.jooq.tables.UserRoles;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRoleRepositoryImpl extends BasicRepositoryImpl implements UserRoleRepository {

    public UserRoleRepositoryImpl(DSLContext dsl) {
        super(dsl);
    }

    @Override
    public void insert(long userId, int roleId) {
        dsl.insertInto(UserRoles.USER_ROLES)
                .set(UserRoles.USER_ROLES.USER_ID, userId)
                .set(UserRoles.USER_ROLES.ROLE_ID, roleId)
                .execute();
    }

    @Override
    public List<RolesRecord> findAllRolesByUserId(long userId) {
        return dsl.select(Roles.ROLES.fields())
                .from(UserRoles.USER_ROLES.join(Roles.ROLES)
                        .on(UserRoles.USER_ROLES.ROLE_ID.eq(Roles.ROLES.ID)))
                .where(UserRoles.USER_ROLES.USER_ID.eq(userId))
                .fetchInto(Roles.ROLES);
    }

}
