package com.weatherforecast.controller;

import com.weatherforecast.exceptions.BadRequestException;
import com.weatherforecast.exceptions.DataConflictException;
import com.weatherforecast.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DataConflictException.class)
    public final ResponseEntity<Object> handleDataConflictException(Exception exception, WebRequest request){
        return handleExceptionInternal(
            exception,
            null,
            new HttpHeaders(),
            HttpStatusCode.valueOf(409),
            request
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> handleMissingElementException(Exception exception, WebRequest request){
        return handleExceptionInternal(
            exception,
            null,
            new HttpHeaders(),
            HttpStatusCode.valueOf(404),
            request
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<Object> handleBadRequestException(Exception exception, WebRequest request){
        return handleExceptionInternal(
            exception,
            null,
            new HttpHeaders(),
            HttpStatusCode.valueOf(400),
            request
        );
    }
}
