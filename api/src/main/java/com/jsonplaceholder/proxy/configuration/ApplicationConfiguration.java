package com.jsonplaceholder.proxy.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@SuppressWarnings("unused")
public class ApplicationConfiguration {

    @Bean
    public URI targetUri(@Value("${client.jsonplaceholder.uri}") String targetUriStr) {
        try {
            return new URI(targetUriStr);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot initialize target uri with `" + targetUriStr + "`", e);
        }
    }

}
