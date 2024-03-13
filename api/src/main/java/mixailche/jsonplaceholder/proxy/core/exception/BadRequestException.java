package mixailche.jsonplaceholder.proxy.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
public class BadRequestException extends RuntimeException {

    private final HttpStatusCode code;

    public BadRequestException() {
        this(HttpStatus.BAD_REQUEST);
    }

}
