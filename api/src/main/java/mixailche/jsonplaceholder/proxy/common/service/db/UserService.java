package mixailche.jsonplaceholder.proxy.common.service.db;

import mixailche.jsonplaceholder.proxy.common.data.UserDto;

import java.util.Optional;

public interface UserService {

    void insert(UserDto user);

    boolean existsByLogin(String login);

    Optional<UserDto> findByLoginAndPasswordSha(String login, String passwordSha);

}
