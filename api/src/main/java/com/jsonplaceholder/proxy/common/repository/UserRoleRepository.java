package com.jsonplaceholder.proxy.common.repository;

import com.jsonplaceholder.proxy.jooq.tables.records.RolesRecord;

import java.util.List;

public interface UserRoleRepository {

    void insert(long userId, int roleId);

    List<RolesRecord> findAllRolesByUserId(long userId);

}
