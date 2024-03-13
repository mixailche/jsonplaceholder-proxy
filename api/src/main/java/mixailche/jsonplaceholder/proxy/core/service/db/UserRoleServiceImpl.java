package mixailche.jsonplaceholder.proxy.core.service.db;

import mixailche.jsonplaceholder.proxy.common.data.AccessLevel;
import mixailche.jsonplaceholder.proxy.common.data.ContentType;
import mixailche.jsonplaceholder.proxy.common.repository.RoleRepository;
import mixailche.jsonplaceholder.proxy.common.repository.UserRepository;
import mixailche.jsonplaceholder.proxy.common.repository.UserRoleRepository;
import mixailche.jsonplaceholder.proxy.common.service.db.UserRoleService;
import mixailche.jsonplaceholder.proxy.core.exception.NotFoundException;
import mixailche.jsonplaceholder.proxy.jooq.tables.records.RolesRecord;
import mixailche.jsonplaceholder.proxy.jooq.tables.records.UsersRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private final UserRoleRepository userRoleRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Override
    public void addUserRole(String userLogin, String roleName) {
        long userId = getOrThrow(userRepository.findByLogin(userLogin), UsersRecord::getId);
        int roleId = getOrThrow(roleRepository.findByName(roleName), RolesRecord::getId);
        userRoleRepository.insert(userId, roleId);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private <T, R> R getOrThrow(Optional<T> optional, Function<T, R> mapper) {
        return optional.map(mapper).orElseThrow(NotFoundException::new);
    }

    @Override
    public Map<ContentType, List<AccessLevel>> getUserPermissionsById(long id) {
        List<RolesRecord> roles = userRoleRepository.findAllRolesByUserId(id);
        Map<ContentType, List<AccessLevel>> result = new HashMap<>();

        for (ContentType type : ContentType.values()) {
            List<AccessLevel> permissions = new ArrayList<>();
            roles.forEach(role -> permissions.add(getRolePermission(role, type)));
            result.put(type, permissions);
        }

        return result;
    }

    private AccessLevel getRolePermission(RolesRecord role, ContentType contentType) {
        var dbColumnValue = switch (contentType) {
            case POSTS  -> role.getPostsAccessLevel();
            case USERS  -> role.getUsersAccessLevel();
            case ALBUMS -> role.getAlbumsAccessLevel();
        };
        // map to business entity
        return switch (dbColumnValue) {
            case NOTHING -> AccessLevel.NOTHING;
            case VIEW    -> AccessLevel.VIEW;
            case EDIT    -> AccessLevel.EDIT;
        };
    }

}
