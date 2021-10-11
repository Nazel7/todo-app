package com.crowntive.todoservice.exceptions;


import com.crowntive.todoservice.dtos.responses.ErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.UnknownHostException;

import javassist.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> resolveUnknownHostException(Exception ex, WebRequest request) {

        if (ex instanceof UnknownHostException){
            return this.resolveUnknownHostException((UnknownHostException) ex, request);
        }
        if (ex instanceof HttpClientErrorException.Forbidden){
            return this.resolveAccessForbiddenException((HttpClientErrorException.Forbidden) ex, request);
        }
        if (ex instanceof NotFoundException){
            return this.resolveNotFoundException((NotFoundException) ex, request);
        }
        if (ex instanceof RuntimeException){
            return this.resolveRuntimeException((RuntimeException) ex, request);
        }

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UnknownHostException.class)
    public final ResponseEntity<ErrorResponse> resolveUnknownHostException(UnknownHostException ex, WebRequest request) {
        ex.printStackTrace();

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_GATEWAY.toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public final ResponseEntity<ErrorResponse> resolveAccessForbiddenException(
            HttpClientErrorException.Forbidden ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.FORBIDDEN.toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ErrorResponse> resolveNotFoundException(
            NotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<ErrorResponse> resolveRuntimeException(
            RuntimeException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
