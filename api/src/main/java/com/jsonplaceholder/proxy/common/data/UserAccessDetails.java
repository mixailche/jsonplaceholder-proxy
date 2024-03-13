package com.jsonplaceholder.proxy.common.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class UserAccessDetails {
    private long userId;
    private Map<ContentType, AccessLevel> permissions;
}
