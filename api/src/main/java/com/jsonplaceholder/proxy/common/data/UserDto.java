package com.jsonplaceholder.proxy.common.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserDto {
    private long id;
    private String login;
    private String passwordSha;
}
