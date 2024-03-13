package mixailche.jsonplaceholder.proxy.common.service.db;

import mixailche.jsonplaceholder.proxy.common.data.AccessLevel;
import mixailche.jsonplaceholder.proxy.common.data.ContentType;

import java.util.List;
import java.util.Map;

public interface UserRoleService {

    Map<ContentType, List<AccessLevel>> getUserPermissionsById(long id);

    void addUserRole(String userLogin, String roleName);

}
