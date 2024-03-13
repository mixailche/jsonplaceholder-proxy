package mixailche.jsonplaceholder.proxy.core.service.business;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import mixailche.jsonplaceholder.proxy.common.data.AccessLevel;
import mixailche.jsonplaceholder.proxy.common.data.ContentType;
import mixailche.jsonplaceholder.proxy.common.data.UserAccessDetails;
import mixailche.jsonplaceholder.proxy.common.service.business.JwtService;
import mixailche.jsonplaceholder.proxy.core.util.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
@SuppressWarnings("unused")
public class JwtServiceImpl implements JwtService {

    private final long tokenLifetime;
    private final String issuer;

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    @Autowired
    public JwtServiceImpl(@Value("${auth.jwt.secret}") String secret,
                          @Value("${auth.jwt.issuer}") String issuer,
                          @Value("${auth.jwt.lifetime.seconds}") long tokenLifetime) {
        this.issuer = issuer;
        this.tokenLifetime = tokenLifetime;
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
    }

    private static class JwtClaims {
        private static final String USER_ID = "userId";
        private static final String PERMISSIONS = "permissions";
    }

    @Override
    public String createToken(UserAccessDetails accessDetails) {
        return JWT.create()
                .withClaim(JwtClaims.USER_ID, accessDetails.getUserId())
                .withClaim(JwtClaims.PERMISSIONS, encodePermissions(
                        accessDetails.getPermissions()
                ))
                .withExpiresAt(getTokenExpirationTime())
                .withIssuer(issuer)
                .sign(algorithm);
    }

    @Override
    public UserAccessDetails getAccessDetails(String token) {
        DecodedJWT decoded = verifier.verify(JWT.decode(token));
        return UserAccessDetails.builder()
                .userId(decoded.getClaim(JwtClaims.USER_ID).asLong())
                .permissions(decodePermissions(
                        decoded.getClaim(JwtClaims.PERMISSIONS).asMap()
                ))
                .build();
    }

    private Date getTokenExpirationTime() {
        return new Date(System.currentTimeMillis() + 1000 * tokenLifetime);
    }

    private Map<String, ?> encodePermissions(Map<ContentType, AccessLevel> permissions) {
        return MapUtils.bimap(permissions, Enum::name, Enum::name);
    }

    private Map<ContentType, AccessLevel> decodePermissions(Map<String, Object> permissions) {
        return MapUtils.bimap(permissions, ContentType::valueOf, value -> AccessLevel.valueOf(value.toString()));
    }

}
