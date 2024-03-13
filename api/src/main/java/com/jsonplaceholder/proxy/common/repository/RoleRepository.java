package com.jsonplaceholder.proxy.common.repository;

import com.jsonplaceholder.proxy.jooq.tables.records.RolesRecord;

import java.util.Optional;

public interface RoleRepository {

    Optional<RolesRecord> findByName(String name);

}
