package com.jsonplaceholder.proxy.core.service.business;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.jsonplaceholder.proxy.common.data.AccessLevel;
import com.jsonplaceholder.proxy.common.data.ContentType;
import com.jsonplaceholder.proxy.common.data.EventResult;
import com.jsonplaceholder.proxy.common.data.ProxyRequestDto;
import com.jsonplaceholder.proxy.common.data.SupportedHttpMethod;
import com.jsonplaceholder.proxy.common.data.UserAccessDetails;
import com.jsonplaceholder.proxy.common.service.business.ContentAccessService;
import com.jsonplaceholder.proxy.common.service.business.JwtService;
import com.jsonplaceholder.proxy.common.service.business.SecurityService;
import com.jsonplaceholder.proxy.common.service.business.JsonPlaceholderClientService;
import com.jsonplaceholder.proxy.common.service.db.EventService;
import com.jsonplaceholder.proxy.core.exception.AccessDeniedException;
import com.jsonplaceholder.proxy.core.exception.AuthorizationFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;

import static com.jsonplaceholder.proxy.core.util.HttpUtils.sendStatusCode;

@Service
@RequiredArgsConstructor
public class ContentAccessServiceImpl implements ContentAccessService {

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final SecurityService securityService;

    @Autowired
    private final JsonPlaceholderClientService jsonPlaceholderClientService;

    @Autowired
    private final EventService eventService;

    @Override
    public ResponseEntity<?> executeProxyRequest(String jwt, ContentType contentType, ProxyRequestDto request) {
        try {
            UserAccessDetails userAccessDetails = jwtService.getAccessDetails(jwt);
            request.setUserId(userAccessDetails.getUserId());
            request.setRequiredAccess(getRequiredAccessLevel(request.getMethod()));
            if (securityService.hasAccess(contentType, request.getRequiredAccess(), userAccessDetails)) {
                return jsonPlaceholderClientService.executeRequest(request);
            } else {
                request.setResult(EventResult.ACCESS_DENIED);
                eventService.saveProcessedProxyRequest(request);
                throw new AccessDeniedException();
            }
        } catch (JWTVerificationException ignored) {
            throw new AuthorizationFailedException();
        }
    }

    private AccessLevel getRequiredAccessLevel(SupportedHttpMethod method) {
        return switch (method) {
            case GET -> AccessLevel.VIEW;
            case POST, PUT, DELETE -> AccessLevel.EDIT;
        };
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Void> handleRestClientException() {
        return sendStatusCode(HttpStatus.NOT_FOUND);
    }

}
