package com.jsonplaceholder.proxy.common.repository;

import com.jsonplaceholder.proxy.jooq.tables.records.UsersRecord;

import java.util.Optional;

public interface UserRepository {

    UsersRecord insert(UsersRecord user);

    boolean existsByLogin(String login);

    Optional<UsersRecord> findByLogin(String login);

    Optional<UsersRecord> findByLoginAndPasswordSha(String login, String passwordSha);

}
