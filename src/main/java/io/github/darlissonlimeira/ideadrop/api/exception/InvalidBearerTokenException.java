package io.github.darlissonlimeira.ideadrop.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidBearerTokenException extends RuntimeException {

    public InvalidBearerTokenException(String message) {
        super(message);
    }
}
