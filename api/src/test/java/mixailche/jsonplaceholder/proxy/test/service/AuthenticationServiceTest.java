package mixailche.jsonplaceholder.proxy.test.service;

import mixailche.jsonplaceholder.proxy.common.data.UserDto;
import mixailche.jsonplaceholder.proxy.common.service.business.AuthenticationService;
import mixailche.jsonplaceholder.proxy.common.service.business.JwtService;
import mixailche.jsonplaceholder.proxy.common.service.business.SecurityService;
import mixailche.jsonplaceholder.proxy.common.service.db.UserService;
import mixailche.jsonplaceholder.proxy.core.exception.LoginAlreadyInUseException;
import mixailche.jsonplaceholder.proxy.core.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.DigestUtils;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthenticationServiceTest {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationServiceTest(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @MockBean
    private JwtService jwtServiceMock;

    @MockBean
    private SecurityService securityServiceMock;

    @MockBean
    private UserService userServiceMock;

    @BeforeEach
    public void setupJwtService() {
        when(jwtServiceMock.createToken(any())).thenReturn("token");
    }

    @ParameterizedTest
    @MethodSource("provideRandomCredentialsSamples")
    public void testEnterExistingUser(String login, String password) {
        prepareUserServiceEnterAnswer(login, password, true);
        Assertions.assertDoesNotThrow(() -> authenticationService.enter(login, password));
    }

    @ParameterizedTest
    @MethodSource("provideRandomCredentialsSamples")
    public void testEnterUserNotFound(String login, String password) {
        prepareUserServiceEnterAnswer(login, password, false);
        Assertions.assertThrows(NotFoundException.class, () -> authenticationService.enter(login, password));
    }

    private void prepareUserServiceEnterAnswer(String login, String password, boolean exists) {
        when(userServiceMock.findByLoginAndPasswordSha(any(), any())).thenAnswer(
                invocation -> exists ==
                        invocation.getArgument(0).equals(login) &&
                        invocation.getArgument(1).equals(getPasswordSha(password))
                                ? Optional.of(sampleUserDto())
                                : Optional.empty()
        );
    }

    @ParameterizedTest
    @MethodSource("provideRandomCredentialsSamples")
    public void testRegisterNewUser(String login, String password) {
        prepareUserServiceRegisterAnswer(login, false);
        Assertions.assertDoesNotThrow(() -> authenticationService.register(login, getPasswordSha(password)));
    }

    @ParameterizedTest
    @MethodSource("provideRandomCredentialsSamples")
    public void testRegisterExistingLogin(String login, String password) {
        prepareUserServiceRegisterAnswer(login, true);
        Assertions.assertThrows(LoginAlreadyInUseException.class,
                () -> authenticationService.register(login, getPasswordSha(password)));
    }

    private void prepareUserServiceRegisterAnswer(String login, boolean alreadyInUse) {
        when(userServiceMock.existsByLogin(any())).thenAnswer(
                invocation -> alreadyInUse == invocation.getArgument(0).equals(login)
        );
    }

    private String getPasswordSha(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

    private UserDto sampleUserDto() {
        return UserDto.builder().build();
    }

    private static final Random RANDOM = new Random();
    private static final int TEST_CASES = 1;

    private static Stream<Arguments> provideRandomCredentialsSamples() {
        return Stream.generate(() -> Arguments.of("login_" + RANDOM.nextInt(), "password_" + RANDOM.nextInt()))
                .limit(TEST_CASES);
    }

}
