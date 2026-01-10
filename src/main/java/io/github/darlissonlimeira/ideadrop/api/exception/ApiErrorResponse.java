package io.github.darlissonlimeira.ideadrop.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ApiErrorResponse {

    private final String error;

    private final String code;

    private final int status;

    private final LocalDateTime timestamp = LocalDateTime.now();

    private final String path;

    public ApiErrorResponse(HttpServletRequest req, HttpStatus status, String error) {
        this.error = error;
        this.status = status.value();
        this.path = req.getRequestURI();
        this.code = status.getReasonPhrase();
    }
}
