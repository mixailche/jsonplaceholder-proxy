package com.jsonplaceholder.proxy.core.controller;

public record AddUserRoleRequest(
        String userLogin,
        String roleName
) {}
