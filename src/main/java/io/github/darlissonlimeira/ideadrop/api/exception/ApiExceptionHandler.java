package io.github.darlissonlimeira.ideadrop.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(MissingRequestCookieException.class)
    public ApiErrorResponse handleMissingRefreshCookie(Exception e, HttpServletRequest request) {
        return new ApiErrorResponse(request, HttpStatus.UNAUTHORIZED, "Missing or Invalid Refresh Token.");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({InvalidBearerTokenException.class})
    public ApiErrorResponse handleInvalidToken(Exception e, HttpServletRequest request) {
        return new ApiErrorResponse(request, HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class})
    public ApiErrorResponse handleResourceNotFound(Exception e, HttpServletRequest request) {
        return new ApiErrorResponse(request, HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UnauthorizedException.class})
    public ApiErrorResponse handleUnauthorized(Exception e, HttpServletRequest request) {
        return new ApiErrorResponse(request, HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({InvalidOwnerException.class})
    public ApiErrorResponse handleInvalidOwner(Exception e, HttpServletRequest request) {
        return new ApiErrorResponse(request, HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ApiErrorResponse handleArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn(e.getMessage());
        return new ApiErrorResponse(request, HttpStatus.UNPROCESSABLE_CONTENT, "Failed Validating Fields.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ApiErrorResponse handleInternalError(Exception e, HttpServletRequest request) {
        log.warn("Cause: {}. Message: {}", e.getClass(), e.getMessage());
        return new ApiErrorResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error.");
    }
}
