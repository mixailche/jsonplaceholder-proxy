package com.jsonplaceholder.proxy.core.controller;

import com.jsonplaceholder.proxy.common.service.db.UserRoleService;
import com.jsonplaceholder.proxy.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.jsonplaceholder.proxy.core.util.HttpUtils.sendStatusCode;

@Controller
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class UserRolesController {

    @Autowired
    private final UserRoleService userRoleService;

    @PostMapping("/user-roles/api/add")
    public ResponseEntity<Void> addUserRole(@RequestBody AddUserRoleRequest request) {
        userRoleService.addUserRole(request.userLogin(), request.roleName());
        return sendStatusCode(HttpStatus.OK);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleDataAccessException() {
        return sendStatusCode(HttpStatus.NOT_FOUND);
    }

}
