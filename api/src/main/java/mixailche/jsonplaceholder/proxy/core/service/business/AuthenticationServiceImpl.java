package mixailche.jsonplaceholder.proxy.core.service.business;

import mixailche.jsonplaceholder.proxy.common.data.UserDto;
import mixailche.jsonplaceholder.proxy.common.service.business.AuthenticationService;
import mixailche.jsonplaceholder.proxy.common.service.business.JwtService;
import mixailche.jsonplaceholder.proxy.common.service.business.SecurityService;
import mixailche.jsonplaceholder.proxy.common.service.db.UserService;
import mixailche.jsonplaceholder.proxy.core.exception.LoginAlreadyInUseException;
import mixailche.jsonplaceholder.proxy.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private final UserService userService;

    @Autowired
    private final SecurityService securityService;

    @Autowired
    private final JwtService jwtService;

    @Override
    public String enter(String login, String password) {
        return userService.findByLoginAndPasswordSha(login, getMd5Digest(password))
                .map(this::createTokenForAuthenticatedUser)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public String register(String login, String password) {
        if (userService.existsByLogin(login)) {
            throw new LoginAlreadyInUseException();
        }

        UserDto user = mapCredentialsToUserDto(login, password);
        userService.insert(user);

        return createTokenForAuthenticatedUser(user);
    }

    private UserDto mapCredentialsToUserDto(String login, String password) {
        return UserDto.builder()
                .login(login)
                .passwordSha(getMd5Digest(password))
                .build();
    }

    private String getMd5Digest(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

    private String createTokenForAuthenticatedUser(UserDto user) {
        return jwtService.createToken(securityService.getAccessDetails(user));
    }

}
