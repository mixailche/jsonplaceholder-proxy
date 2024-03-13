package mixailche.jsonplaceholder.proxy.common.repository;

import mixailche.jsonplaceholder.proxy.jooq.tables.records.RolesRecord;

import java.util.List;

public interface UserRoleRepository {

    void insert(long userId, int roleId);

    List<RolesRecord> findAllRolesByUserId(long userId);

}
