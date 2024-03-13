package mixailche.jsonplaceholder.proxy.core.repository;

import mixailche.jsonplaceholder.proxy.common.repository.UserRepository;
import mixailche.jsonplaceholder.proxy.jooq.tables.records.UsersRecord;
import mixailche.jsonplaceholder.proxy.jooq.tables.Users;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.jooq.impl.DSL.selectOne;

@Repository
public class UserRepositoryImpl extends BasicRepositoryImpl implements UserRepository {

    public UserRepositoryImpl(DSLContext dsl) {
        super(dsl);
    }

    @Override
    public UsersRecord insert(UsersRecord user) {
        return dsl.insertInto(Users.USERS)
                .set(user)
                .returning()
                .fetchOne();
    }

    @Override
    public boolean existsByLogin(String login) {
        return dsl.fetchExists(
                selectOne()
                        .from(Users.USERS)
                        .where(Users.USERS.LOGIN.eq(login))
        );
    }

    @Override
    public Optional<UsersRecord> findByLogin(String login) {
        return dsl.selectFrom(Users.USERS)
                .where(Users.USERS.LOGIN.eq(login))
                .stream().findAny();
    }

    @Override
    public Optional<UsersRecord> findByLoginAndPasswordSha(String login, String passwordSha) {
        return dsl.selectFrom(Users.USERS)
                .where(Users.USERS.LOGIN.eq(login)
                        .and(Users.USERS.PASSWORD_SHA.eq(passwordSha)))
                .stream().findAny();
    }

}
