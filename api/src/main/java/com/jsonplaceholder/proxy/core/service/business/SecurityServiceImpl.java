package com.jsonplaceholder.proxy.core.service.business;

import com.jsonplaceholder.proxy.common.data.AccessLevel;
import com.jsonplaceholder.proxy.common.data.ContentType;
import com.jsonplaceholder.proxy.common.data.UserAccessDetails;
import com.jsonplaceholder.proxy.common.data.UserDto;
import com.jsonplaceholder.proxy.common.service.business.SecurityService;
import com.jsonplaceholder.proxy.common.service.db.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.jsonplaceholder.proxy.core.util.MapUtils.mapValues;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private final UserRoleService userRoleService;

    @Override
    public UserAccessDetails getAccessDetails(UserDto user) {
        return getAccessDetails(user.getId());
    }

    private UserAccessDetails getAccessDetails(long userId) {
        Map<ContentType, AccessLevel> permissions = mapValues(
                userRoleService.getUserPermissionsById(userId),
                this::getStrongestPermission
        );

        return UserAccessDetails.builder()
                .userId(userId)
                .permissions(permissions)
                .build();
    }

    private AccessLevel getStrongestPermission(List<AccessLevel> permissions) {
        return permissions.stream()
                .max(Comparator.naturalOrder())
                .orElse(AccessLevel.NOTHING);
    }

    @Override
    public boolean hasAccess(ContentType contentType, AccessLevel requiredAccess, UserAccessDetails accessDetails) {
        AccessLevel presentLevel = Objects.requireNonNullElse(
                accessDetails.getPermissions().get(contentType),
                AccessLevel.NOTHING
        );
        return presentLevel.compareTo(requiredAccess) >= 0;
    }

}
