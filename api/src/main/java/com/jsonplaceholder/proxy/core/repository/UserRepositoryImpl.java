package com.jsonplaceholder.proxy.core.repository;

import com.jsonplaceholder.proxy.common.repository.UserRepository;
import com.jsonplaceholder.proxy.jooq.tables.records.UsersRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.jsonplaceholder.proxy.jooq.tables.Users.USERS;
import static org.jooq.impl.DSL.selectOne;

@Repository
public class UserRepositoryImpl extends BasicRepositoryImpl implements UserRepository {

    public UserRepositoryImpl(DSLContext dsl) {
        super(dsl);
    }

    @Override
    public UsersRecord insert(UsersRecord user) {
        return dsl.insertInto(USERS)
                .set(user)
                .returning()
                .fetchOne();
    }

    @Override
    public boolean existsByLogin(String login) {
        return dsl.fetchExists(
                selectOne()
                        .from(USERS)
                        .where(USERS.LOGIN.eq(login))
        );
    }

    @Override
    public Optional<UsersRecord> findByLogin(String login) {
        return dsl.selectFrom(USERS)
                .where(USERS.LOGIN.eq(login))
                .stream().findAny();
    }

    @Override
    public Optional<UsersRecord> findByLoginAndPasswordSha(String login, String passwordSha) {
        return dsl.selectFrom(USERS)
                .where(USERS.LOGIN.eq(login)
                        .and(USERS.PASSWORD_SHA.eq(passwordSha)))
                .stream().findAny();
    }

}
