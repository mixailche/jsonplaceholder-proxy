package mixailche.jsonplaceholder.proxy.test.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import mixailche.jsonplaceholder.proxy.common.data.AccessLevel;
import mixailche.jsonplaceholder.proxy.common.data.ContentType;
import mixailche.jsonplaceholder.proxy.common.data.ProxyRequestDto;
import mixailche.jsonplaceholder.proxy.common.data.SupportedHttpMethod;
import mixailche.jsonplaceholder.proxy.common.data.UserAccessDetails;
import mixailche.jsonplaceholder.proxy.common.service.business.ContentAccessService;
import mixailche.jsonplaceholder.proxy.common.service.business.JsonPlaceholderClientService;
import mixailche.jsonplaceholder.proxy.common.service.business.JwtService;
import mixailche.jsonplaceholder.proxy.common.service.business.SecurityService;
import mixailche.jsonplaceholder.proxy.common.service.db.EventService;
import mixailche.jsonplaceholder.proxy.core.exception.AccessDeniedException;
import mixailche.jsonplaceholder.proxy.core.exception.AuthorizationFailedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Map;
import java.util.stream.Stream;

import static mixailche.jsonplaceholder.proxy.common.data.AccessLevel.EDIT;
import static mixailche.jsonplaceholder.proxy.common.data.AccessLevel.NOTHING;
import static mixailche.jsonplaceholder.proxy.common.data.AccessLevel.VIEW;
import static mixailche.jsonplaceholder.proxy.common.data.SupportedHttpMethod.DELETE;
import static mixailche.jsonplaceholder.proxy.common.data.SupportedHttpMethod.GET;
import static mixailche.jsonplaceholder.proxy.common.data.SupportedHttpMethod.POST;
import static mixailche.jsonplaceholder.proxy.common.data.SupportedHttpMethod.PUT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ContentAccessServiceTest {

    private final ContentAccessService contentAccessService;

    @Autowired
    public ContentAccessServiceTest(ContentAccessService contentAccessService) {
        this.contentAccessService = contentAccessService;
    }

    @MockBean
    private SecurityService securityServiceMock;

    @MockBean
    private JsonPlaceholderClientService jsonPlaceholderClientServiceMock;

    @MockBean
    private JwtService jwtServiceMock;

    @MockBean
    private EventService eventServiceMock;

    @Test
    public void testWhenHasAccess() {
        when(jwtServiceMock.getAccessDetails(any())).thenReturn(UserAccessDetails.builder().build());
        when(securityServiceMock.hasAccess(any(), any(), any())).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> contentAccessService.executeProxyRequest(null, null, sampleRequest()));
    }

    @ParameterizedTest
    @MethodSource("provideAccessControlSamples")
    public void testAccessControl(AccessLevel presentLevel, SupportedHttpMethod method, boolean shouldFail) {
        ContentType contentType = ContentType.ALBUMS;
        ProxyRequestDto request = sampleRequest();
        request.setMethod(method);

        when(securityServiceMock.hasAccess(any(), any(), any())).thenAnswer(
                invocation -> hasAccess(
                        invocation.getArgument(0),
                        invocation.getArgument(1),
                        invocation.getArgument(2)
                )
        );

        when(jwtServiceMock.getAccessDetails(any()))
                .thenReturn(UserAccessDetails.builder()
                        .permissions(Map.of(contentType, presentLevel))
                        .build());

        Executable action = () -> contentAccessService.executeProxyRequest(null, contentType, request);
        if (shouldFail) {
            Assertions.assertThrows(AccessDeniedException.class, action);
        } else {
            Assertions.assertDoesNotThrow(action);
        }
    }

    private boolean hasAccess(ContentType contentType, AccessLevel requiredAccess, UserAccessDetails accessDetails) {
        AccessLevel presentLevel = accessDetails.getPermissions().getOrDefault(contentType, AccessLevel.NOTHING);
        return presentLevel.compareTo(requiredAccess) >= 0;
    }

    @Test
    public void testJwtErrorForwarding() {
        when(jwtServiceMock.getAccessDetails(any())).thenThrow(JWTVerificationException.class);
        Assertions.assertThrows(AuthorizationFailedException.class,
                () -> contentAccessService.executeProxyRequest(null, null, sampleRequest()));
    }

    private static Stream<Arguments> provideAccessControlSamples() {
        return Stream.of(
                Arguments.of(NOTHING, GET, true),
                Arguments.of(NOTHING, POST, true),
                Arguments.of(NOTHING, PUT, true),
                Arguments.of(NOTHING, DELETE, true),
                Arguments.of(VIEW, GET, false),
                Arguments.of(VIEW, POST, true),
                Arguments.of(VIEW, PUT, true),
                Arguments.of(VIEW, DELETE, true),
                Arguments.of(EDIT, GET, false),
                Arguments.of(EDIT, POST, false),
                Arguments.of(EDIT, PUT, false),
                Arguments.of(EDIT, DELETE, false)
        );
    }

    private ProxyRequestDto sampleRequest() {
        return ProxyRequestDto.builder()
                .body("sample body")
                .contextPath("/sample/path")
                .requiredAccess(EDIT)
                .method(POST)
                .build();
    }

}
