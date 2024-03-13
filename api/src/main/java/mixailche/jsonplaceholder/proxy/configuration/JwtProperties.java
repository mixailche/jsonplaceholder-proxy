package mixailche.jsonplaceholder.proxy.configuration;

import lombok.Builder;

@Builder
public record JwtProperties(
        String secret,
        String issuer,
        long tokenLifetime
) {}
