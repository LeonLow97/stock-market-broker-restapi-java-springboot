package com.stockmarket.stockmarketapi.exceptiontests;

import com.stockmarket.stockmarketapi.exception.ApiExceptionHandler;
import com.stockmarket.stockmarketapi.exception.ApiException;
import com.stockmarket.stockmarketapi.exception.BadRequestException;
import com.stockmarket.stockmarketapi.exception.OrderNotFilledException;
import com.stockmarket.stockmarketapi.exception.ResourceAlreadyExistsException;
import com.stockmarket.stockmarketapi.exception.ResourceNotFoundException;
import com.stockmarket.stockmarketapi.exception.AuthException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiExceptionHandlerTests {
    private ApiExceptionHandler apiExceptionHandler;

    @BeforeEach
    public void setup() {
        apiExceptionHandler = new ApiExceptionHandler();
    }

    @Test
    public void handleBadRequestException_shouldReturnBadRequest() {
        // Arrange
        String errorMessage = "Bad Request";
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Z"));
        BadRequestException badRequestException = new BadRequestException(errorMessage);

        // Act
        ResponseEntity<Object> response =
                apiExceptionHandler.handleBadRequestException(badRequestException);
        ApiException apiException = (ApiException) response.getBody();

        // Assert
        assertEquals(expectedStatus, response.getStatusCode());
        assertEquals(errorMessage, apiException.getMessage());
        assertEquals(expectedStatus, apiException.getHttpStatus());
        assertEquals(now.toEpochSecond(), apiException.getTimestamp().toEpochSecond());
    }

    @Test
    public void testHandleAuthRequestException() {
        // Arrange
        AuthException e = new AuthException("Unauthorized request");
        HttpStatus expectedHttpStatus = HttpStatus.UNAUTHORIZED;
        ZonedDateTime expectedTimestamp = ZonedDateTime.now(ZoneId.of("Z"));

        ApiExceptionHandler apiExceptionHandler = new ApiExceptionHandler();

        // Act
        ResponseEntity<Object> responseEntity = apiExceptionHandler.handleAuthRequestException(e);
        ApiException apiException = (ApiException) responseEntity.getBody();

        // Assert
        assertEquals(expectedHttpStatus, responseEntity.getStatusCode());
        assertEquals("Unauthorized request", apiException.getMessage());
        assertEquals(expectedHttpStatus, apiException.getHttpStatus());
        assertEquals(expectedTimestamp.getHour(), apiException.getTimestamp().getHour());
        assertEquals(expectedTimestamp.getMinute(), apiException.getTimestamp().getMinute());
        assertEquals(expectedTimestamp.getZone(), apiException.getTimestamp().getZone());
    }

    @Test
    public void testHandleOrderNotFilledException() {
        // Arrange
        OrderNotFilledException e = new OrderNotFilledException("Order not filled");
        HttpStatus notFoundRequest = HttpStatus.NOT_FOUND;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Z"));

        // Act
        ResponseEntity<Object> response = apiExceptionHandler.handleOrderNotFilledException(e);
        ApiException apiException = (ApiException) response.getBody();

        // Assert
        assertNotNull(apiException);
        assertEquals("Order not filled", apiException.getMessage());
        assertEquals(notFoundRequest, apiException.getHttpStatus());
        assertEquals(now.getHour(), apiException.getTimestamp().getHour());
    }

    @Test
    public void testHandleResourceAlreadyExistsException() {
        // Arrange
        ResourceAlreadyExistsException e =
                new ResourceAlreadyExistsException("Resource already exists");
        HttpStatus conflictRequest = HttpStatus.CONFLICT;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Z"));

        // Act
        ResponseEntity<Object> response =
                apiExceptionHandler.handleResourceAlreadyExistsException(e);
        ApiException apiException = (ApiException) response.getBody();

        // Assert
        assertNotNull(apiException);
        assertEquals("Resource already exists", apiException.getMessage());
        assertEquals(conflictRequest, apiException.getHttpStatus());
        assertEquals(now.getHour(), apiException.getTimestamp().getHour());
    }

    @Test
    public void testHandleResourceNotFoundException() {
        // Arrange
        ResourceNotFoundException e = new ResourceNotFoundException("Resource not found");
        HttpStatus notFoundRequest = HttpStatus.CONFLICT;
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Z"));

        // Act
        ResponseEntity<Object> response = apiExceptionHandler.handleResourceNotFoundException(e);
        ApiException apiException = (ApiException) response.getBody();

        // Assert
        assertNotNull(apiException);
        assertEquals("Resource not found", apiException.getMessage());
        assertEquals(notFoundRequest, apiException.getHttpStatus());
        assertEquals(now.getHour(), apiException.getTimestamp().getHour());
    }

}
