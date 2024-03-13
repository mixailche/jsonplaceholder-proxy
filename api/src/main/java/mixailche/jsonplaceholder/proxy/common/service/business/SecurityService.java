package mixailche.jsonplaceholder.proxy.common.service.business;

import mixailche.jsonplaceholder.proxy.common.data.AccessLevel;
import mixailche.jsonplaceholder.proxy.common.data.ContentType;
import mixailche.jsonplaceholder.proxy.common.data.UserAccessDetails;
import mixailche.jsonplaceholder.proxy.common.data.UserDto;

public interface SecurityService {

    UserAccessDetails getAccessDetails(UserDto user);

    boolean hasAccess(ContentType contentType, AccessLevel requiredAccess, UserAccessDetails accessDetails);

}
