package com.jsonplaceholder.proxy.common.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SupportedHttpMethod {
    GET(true, true),
    POST(false, false),
    PUT(false, true),
    DELETE(false, true);

    private final boolean safe;

    private final boolean idempotent;

}
