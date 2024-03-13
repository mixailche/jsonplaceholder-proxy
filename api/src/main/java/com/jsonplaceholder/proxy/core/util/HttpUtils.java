package com.jsonplaceholder.proxy.core.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class HttpUtils {

    public static ResponseEntity<Void> sendStatusCode(HttpStatusCode code) {
        return ResponseEntity.status(code).build();
    }

}
