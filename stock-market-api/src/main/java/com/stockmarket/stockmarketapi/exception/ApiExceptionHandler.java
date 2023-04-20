package com.stockmarket.stockmarketapi.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e) {
        // Create payload containing exception details
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException =
                new ApiException(e.getMessage(), badRequest, ZonedDateTime.now(ZoneId.of("Z")));
        // return the response entity
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {AuthException.class})
    public ResponseEntity<Object> handleAuthRequestException(AuthException e) {
        HttpStatus unauthorizedRequest = HttpStatus.UNAUTHORIZED;
        ApiException apiException = new ApiException(e.getMessage(), unauthorizedRequest,
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, unauthorizedRequest);
    }

    @ExceptionHandler(value = {OrderNotFilledException.class})
    public ResponseEntity<Object> handleOrderNotFilledException(OrderNotFilledException e) {
        HttpStatus notFoundRequest = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(e.getMessage(), notFoundRequest,
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, notFoundRequest);
    }

    @ExceptionHandler(value = {ResourceAlreadyExistsException.class})
    public ResponseEntity<Object> handleResourceAlreadyExistsException(
            ResourceAlreadyExistsException e) {
        HttpStatus conflictRequest = HttpStatus.CONFLICT;
        ApiException apiException = new ApiException(e.getMessage(), conflictRequest,
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, conflictRequest);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e) {
        HttpStatus notFoundRequest = HttpStatus.CONFLICT;
        ApiException apiException = new ApiException(e.getMessage(), notFoundRequest,
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, notFoundRequest);
    }

    @ExceptionHandler(value = {InternalServerErrorException.class})
    public ResponseEntity<Object> handleInternalServerErrorException(
            InternalServerErrorException e) {
        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiException apiException = new ApiException(e.getMessage(), internalServerError,
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, internalServerError);
    }
}
