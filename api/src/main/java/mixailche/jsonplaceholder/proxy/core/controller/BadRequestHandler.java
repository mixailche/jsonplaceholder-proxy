package mixailche.jsonplaceholder.proxy.core.controller;

import mixailche.jsonplaceholder.proxy.core.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static mixailche.jsonplaceholder.proxy.core.util.HttpUtils.sendStatusCode;

@ControllerAdvice
@SuppressWarnings("unused")
public class BadRequestHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Void> handleBadRequest(BadRequestException e) {
        return sendStatusCode(e.getCode());
    }

}
