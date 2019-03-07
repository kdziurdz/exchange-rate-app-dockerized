package pl.parser.nbp.infrastructure.support.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_GATEWAY)
public class ExternalApiException extends RuntimeException {
    private final Object errorCode;

    public enum ErrorCode {
        EXTERNAL_API_EXCEPTION,
        EXTERNAL_API_UNEXPECTED_RESPONSE_VALUE
    }

    public ExternalApiException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public Object getErrorCode() {
        return this.errorCode;
    }
}
