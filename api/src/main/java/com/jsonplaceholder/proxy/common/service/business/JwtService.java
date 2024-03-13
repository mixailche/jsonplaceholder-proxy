package com.jsonplaceholder.proxy.common.service.business;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.jsonplaceholder.proxy.common.data.UserAccessDetails;

public interface JwtService {

    String createToken(UserAccessDetails accessDetails);

    UserAccessDetails getAccessDetails(String token) throws JWTVerificationException;

}
