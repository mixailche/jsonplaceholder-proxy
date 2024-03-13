package com.jsonplaceholder.proxy.common.service.business;

import com.jsonplaceholder.proxy.common.data.AccessLevel;
import com.jsonplaceholder.proxy.common.data.ContentType;
import com.jsonplaceholder.proxy.common.data.UserAccessDetails;
import com.jsonplaceholder.proxy.common.data.UserDto;

public interface SecurityService {

    UserAccessDetails getAccessDetails(UserDto user);

    boolean hasAccess(ContentType contentType, AccessLevel requiredAccess, UserAccessDetails accessDetails);

}
