package com.pragma.traceability.infrastructure.exceptionhandler;

import com.pragma.traceability.domain.exception.DomainException;
import com.pragma.traceability.domain.exception.FoodCourtServiceUnavailableException;
import com.pragma.traceability.domain.exception.NotRestaurantOwnerException;
import com.pragma.traceability.domain.exception.OrderTraceNotFoundException;
import com.pragma.traceability.domain.exception.RestaurantNotFoundException;
import com.pragma.traceability.domain.exception.TraceNotFromClientException;
import com.pragma.traceability.infrastructure.exception.NoDataFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {

    private static final String MESSAGE = "message";

    @ExceptionHandler(OrderTraceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOrderTraceNotFoundException(
            OrderTraceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, exception.getMessage()));
    }

    @ExceptionHandler(TraceNotFromClientException.class)
    public ResponseEntity<Map<String, String>> handleTraceNotFromClientException(
            TraceNotFromClientException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(MESSAGE, exception.getMessage()));
    }

    @ExceptionHandler(NotRestaurantOwnerException.class)
    public ResponseEntity<Map<String, String>> handleNotRestaurantOwnerException(
            NotRestaurantOwnerException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(MESSAGE, exception.getMessage()));
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRestaurantNotFoundException(
            RestaurantNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, exception.getMessage()));
    }

    @ExceptionHandler(FoodCourtServiceUnavailableException.class)
    public ResponseEntity<Map<String, String>> handleFoodCourtServiceUnavailableException(
            FoodCourtServiceUnavailableException exception) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(Collections.singletonMap(MESSAGE, exception.getMessage()));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, String>> handleDomainException(DomainException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, exception.getMessage()));
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoDataFoundException(
            NoDataFoundException ignoredNoDataFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.NO_DATA_FOUND.getMessage()));
    }
}
