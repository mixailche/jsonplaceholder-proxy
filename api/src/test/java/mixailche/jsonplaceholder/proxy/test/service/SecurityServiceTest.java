package mixailche.jsonplaceholder.proxy.test.service;

import mixailche.jsonplaceholder.proxy.common.data.AccessLevel;
import mixailche.jsonplaceholder.proxy.common.data.ContentType;
import mixailche.jsonplaceholder.proxy.common.data.UserAccessDetails;
import mixailche.jsonplaceholder.proxy.common.data.UserDto;
import mixailche.jsonplaceholder.proxy.common.service.business.SecurityService;
import mixailche.jsonplaceholder.proxy.common.service.db.UserRoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import static mixailche.jsonplaceholder.proxy.common.data.AccessLevel.EDIT;
import static mixailche.jsonplaceholder.proxy.common.data.AccessLevel.NOTHING;
import static mixailche.jsonplaceholder.proxy.common.data.AccessLevel.VIEW;
import static mixailche.jsonplaceholder.proxy.common.data.ContentType.ALBUMS;
import static mixailche.jsonplaceholder.proxy.common.data.ContentType.POSTS;
import static mixailche.jsonplaceholder.proxy.common.data.ContentType.USERS;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SecurityServiceTest {

    private final SecurityService securityService;

    @Autowired
    public SecurityServiceTest(SecurityService securityService) {
        this.securityService = securityService;
    }

    @MockBean
    private UserRoleService userRoleServiceMock;

    @ParameterizedTest
    @MethodSource("providePermissionResolvingSamples")
    public void testPermissionsResolving(Map<ContentType, List<AccessLevel>> permissions,
                                         ContentType contentType, AccessLevel expectedAccessLevel) {
        UserDto user = sampleUserWithId();
        when(userRoleServiceMock.getUserPermissionsById(user.getId())).thenReturn(permissions);
        Map<ContentType, AccessLevel> resolved = securityService.getAccessDetails(user).getPermissions();
        Assertions.assertEquals(expectedAccessLevel, resolved.getOrDefault(contentType, NOTHING));
    }

    @ParameterizedTest
    @MethodSource("provideAccessMapSamples")
    public void testAccessResolving(Map<ContentType, AccessLevel> permissions, ContentType contentType,
                                    AccessLevel requiredAccessLevel, Boolean expectedAnswer) {
        UserAccessDetails accessDetails = UserAccessDetails.builder()
                .userId(sampleUserWithId().getId())
                .permissions(permissions)
                .build();
        Assertions.assertEquals(securityService.hasAccess(contentType, requiredAccessLevel, accessDetails), expectedAnswer);
    }

    private static final Random RANDOM = new Random();

    private UserDto sampleUserWithId() {
        return UserDto.builder().id(RANDOM.nextLong()).build();
    }

    private static Stream<Arguments> providePermissionResolvingSamples() {
        return Stream.of(
                Arguments.of(Map.of(),                                  POSTS,  NOTHING),
                Arguments.of(Map.of(USERS,  List.of(NOTHING)),          USERS,  NOTHING),
                Arguments.of(Map.of(ALBUMS, List.of()),                 ALBUMS, NOTHING),
                Arguments.of(Map.of(POSTS,  List.of(EDIT)),             ALBUMS, NOTHING),
                Arguments.of(Map.of(POSTS,  List.of(NOTHING, VIEW)),    POSTS,  VIEW),
                Arguments.of(Map.of(POSTS,  List.of(VIEW, EDIT)),       POSTS,  EDIT),
                Arguments.of(Map.of(USERS,  List.of(VIEW, VIEW)),       USERS,  VIEW),
                Arguments.of(Map.of(USERS,  List.of(EDIT, EDIT, EDIT)), USERS,  EDIT),
                Arguments.of(Map.of(ALBUMS, List.of(VIEW)),             ALBUMS, VIEW),
                Arguments.of(Map.of(ALBUMS, List.of(EDIT)),             ALBUMS, EDIT)
        );
    }

    private static Stream<Arguments> provideAccessMapSamples() {
        return Stream.of(
                Arguments.of(Map.of(),                POSTS,  NOTHING, true),
                Arguments.of(Map.of(USERS,  NOTHING), USERS,  NOTHING, true),
                Arguments.of(Map.of(POSTS,  NOTHING), ALBUMS, NOTHING, true),
                Arguments.of(Map.of(POSTS,  EDIT),    ALBUMS, VIEW,    false),
                Arguments.of(Map.of(POSTS,  VIEW),    POSTS,  EDIT,    false),
                Arguments.of(Map.of(POSTS,  EDIT),    POSTS,  EDIT,    true),
                Arguments.of(Map.of(USERS,  VIEW),    USERS,  VIEW,    true),
                Arguments.of(Map.of(USERS,  EDIT),    USERS,  NOTHING, true),
                Arguments.of(Map.of(ALBUMS, VIEW),    ALBUMS, VIEW,    true),
                Arguments.of(Map.of(ALBUMS, VIEW),    ALBUMS, EDIT,    false)
        );
    }

}
