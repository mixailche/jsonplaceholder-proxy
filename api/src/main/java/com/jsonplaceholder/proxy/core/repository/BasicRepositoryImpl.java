package com.jsonplaceholder.proxy.core.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor
public class BasicRepositoryImpl {

    @Autowired
    protected final DSLContext dsl;

}
