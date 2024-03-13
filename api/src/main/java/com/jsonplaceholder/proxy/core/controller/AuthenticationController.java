package com.jsonplaceholder.proxy.core.controller;

import com.jsonplaceholder.proxy.common.service.business.AuthenticationService;
import com.jsonplaceholder.proxy.core.exception.LoginAlreadyInUseException;
import com.jsonplaceholder.proxy.core.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.jsonplaceholder.proxy.core.util.HttpUtils.sendStatusCode;

@RestController
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class AuthenticationController {

    @Autowired
    private final AuthenticationService authenticationService;

    @PostMapping("/auth/api/enter")
    public String enter(@RequestBody UserCredentials credentials) {
        return authenticationService.enter(credentials.login(), credentials.password());
    }

    @PostMapping("/auth/api/register")
    public String register(@RequestBody UserCredentials credentials) {
        return authenticationService.register(credentials.login(), credentials.password());
    }

    @ExceptionHandler({
            NotFoundException.class,
            LoginAlreadyInUseException.class
    })
    public ResponseEntity<Void> handleAuthenticationError() {
        return sendStatusCode(HttpStatus.BAD_REQUEST);
    }

}
