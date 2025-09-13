package com.example.bcitest.advice;

import com.example.bcitest.userinterface.dto.ErrorDetail;
import com.example.bcitest.userinterface.dto.ErrorResponse;
import com.example.bcitest.userinterface.exception.CredentialsNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ErrorDetail errorDetail = new ErrorDetail(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage()
        );
        ErrorResponse errorResponse = new ErrorResponse(Collections.singletonList(errorDetail));
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorDetail errorDetail = new ErrorDetail(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        ErrorResponse errorResponse = new ErrorResponse(Collections.singletonList(errorDetail));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorDetail errorDetail = new ErrorDetail(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        ErrorResponse errorResponse = new ErrorResponse(Collections.singletonList(errorDetail));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CredentialsNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(CredentialsNotFoundException ex) {
        ErrorDetail errorDetail = new ErrorDetail(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        ErrorResponse errorResponse = new ErrorResponse(Collections.singletonList(errorDetail));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(JwtException ex) {
        ErrorDetail errorDetail = new ErrorDetail(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage()
        );
        ErrorResponse errorResponse = new ErrorResponse(Collections.singletonList(errorDetail));
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

}
