package mixailche.jsonplaceholder.proxy.test.configuration;

import mixailche.jsonplaceholder.proxy.configuration.JwtProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class UnitTestConfiguration {

    public static final long REDUCED_TOKEN_LIFETIME = 1; // sec

    @Bean
    @Primary
    public JwtProperties reducedLifetimeTimeJwtProperties(@Value("${auth.jwt.secret}") String secret,
                                                          @Value("${auth.jwt.issuer}") String issuer) {
        return JwtProperties.builder()
                .secret(secret)
                .issuer(issuer)
                .tokenLifetime(REDUCED_TOKEN_LIFETIME)
                .build();
    }

}
