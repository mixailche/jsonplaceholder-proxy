package mixailche.jsonplaceholder.proxy.core.controller;

import mixailche.jsonplaceholder.proxy.common.data.ContentType;
import mixailche.jsonplaceholder.proxy.common.data.ProxyRequestDto;
import mixailche.jsonplaceholder.proxy.common.data.SupportedHttpMethod;
import mixailche.jsonplaceholder.proxy.common.service.business.ContentAccessService;
import mixailche.jsonplaceholder.proxy.core.exception.AccessDeniedException;
import mixailche.jsonplaceholder.proxy.core.exception.AuthorizationFailedException;
import mixailche.jsonplaceholder.proxy.core.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import static mixailche.jsonplaceholder.proxy.core.util.HttpUtils.sendStatusCode;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class ContentAccessController {

    @Autowired
    private final ContentAccessService contentAccessService;

    @RequestMapping(
            path = "/api/{contentTypeName}/**",
            method = { GET, POST, PUT, DELETE }
    )
    public ResponseEntity<?> executeProxyRequest(@PathVariable String contentTypeName,
                                                 RequestEntity<String> request) {
        MultiValueMap<String, String> headers = HttpHeaders.writableHttpHeaders(request.getHeaders());
        return contentAccessService.executeProxyRequest(
                extractJwt(headers),
                mapNameToContentType(contentTypeName),
                ProxyRequestDto.builder()
                        .method(getSupportedMethod(request))
                        .contextPath(getContextPath(request.getUrl()))
                        .headers(headers)
                        .body(request.getBody())
                        .build()
        );
    }

    private String extractJwt(MultiValueMap<String, String> headers) {
        List<String> values = headers.remove("X-AuthToken");
        if (values.isEmpty()) {
            throw new BadRequestException(HttpStatus.UNAUTHORIZED);
        }
        return values.get(0);
    }

    private SupportedHttpMethod getSupportedMethod(RequestEntity<?> request) {
        HttpMethod method = request.getMethod();
        if (method == null) {
            throw new BadRequestException();
        }
        return switch (method.name()) {
            case "GET"    -> SupportedHttpMethod.GET;
            case "POST"   -> SupportedHttpMethod.POST;
            case "PUT"    -> SupportedHttpMethod.PUT;
            case "DELETE" -> SupportedHttpMethod.DELETE;
            default -> throw new BadRequestException(HttpStatus.NOT_IMPLEMENTED);
        };
    }

    private ContentType mapNameToContentType(String name) {
        return switch (name) {
            case "posts"  -> ContentType.POSTS;
            case "users"  -> ContentType.USERS;
            case "albums" -> ContentType.ALBUMS;
            default -> throw new BadRequestException(HttpStatus.NOT_FOUND);
        };
    }

    private static final String PATH_PREFIX = "api/";

    private String getContextPath(URI uri) {
        String query = uri.getQuery();
        return uri.getPath().substring(PATH_PREFIX.length()) + "?" + Objects.requireNonNullElse(uri.getQuery(), "");
    }

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<Void> handleFailedAuthorization() {
        return sendStatusCode(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDenied() {
        return sendStatusCode(HttpStatus.FORBIDDEN);
    }

}
