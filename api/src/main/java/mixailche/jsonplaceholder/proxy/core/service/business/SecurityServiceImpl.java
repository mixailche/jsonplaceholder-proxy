package mixailche.jsonplaceholder.proxy.core.service.business;

import mixailche.jsonplaceholder.proxy.common.data.AccessLevel;
import mixailche.jsonplaceholder.proxy.common.data.ContentType;
import mixailche.jsonplaceholder.proxy.common.data.UserAccessDetails;
import mixailche.jsonplaceholder.proxy.common.data.UserDto;
import mixailche.jsonplaceholder.proxy.common.service.business.SecurityService;
import mixailche.jsonplaceholder.proxy.common.service.db.UserRoleService;
import lombok.RequiredArgsConstructor;
import mixailche.jsonplaceholder.proxy.core.util.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        Map<ContentType, AccessLevel> permissions = MapUtils.mapValues(
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
        AccessLevel presentLevel = accessDetails.getPermissions().getOrDefault(contentType, AccessLevel.NOTHING);
        return presentLevel.compareTo(requiredAccess) >= 0;
    }

}
