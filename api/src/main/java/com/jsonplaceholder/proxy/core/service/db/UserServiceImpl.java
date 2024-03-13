package com.jsonplaceholder.proxy.core.service.db;

import com.jsonplaceholder.proxy.common.data.UserDto;
import com.jsonplaceholder.proxy.common.repository.UserRepository;
import com.jsonplaceholder.proxy.common.service.db.UserService;
import com.jsonplaceholder.proxy.jooq.tables.records.UsersRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Override
    public void insert(UserDto user) {
        UsersRecord record = new UsersRecord();
        record.setLogin(user.getLogin());
        record.setPasswordSha(user.getPasswordSha());
        long id = userRepository.insert(record).getId();
        user.setId(id);
    }

    @Override
    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    @Override
    public Optional<UserDto> findByLoginAndPasswordSha(String login, String passwordSha) {
        return userRepository.findByLoginAndPasswordSha(login, passwordSha)
                .map(this::mapRecordToDto);
    }

    private UserDto mapRecordToDto(UsersRecord record) {
        return UserDto.builder()
                .id(record.getId())
                .login(record.getLogin())
                .passwordSha(record.getPasswordSha())
                .build();
    }

}
