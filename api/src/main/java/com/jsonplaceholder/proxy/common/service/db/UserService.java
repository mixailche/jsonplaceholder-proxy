package com.jsonplaceholder.proxy.common.service.db;

import com.jsonplaceholder.proxy.common.data.UserDto;

import java.util.Optional;

public interface UserService {

    void insert(UserDto user);

    boolean existsByLogin(String login);

    Optional<UserDto> findByLoginAndPasswordSha(String login, String passwordSha);

}
