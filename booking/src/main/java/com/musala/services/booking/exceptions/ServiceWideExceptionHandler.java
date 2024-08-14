package com.musala.services.booking.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musala.services.booking.models.responses.ExceptionResponse;
import com.musala.services.booking.models.responses.Response;

@RestControllerAdvice
public class ServiceWideExceptionHandler {

    private ObjectMapper objectMapper = new ObjectMapper();
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleException(Exception ex) throws JsonProcessingException {
        String message = null;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if(ex instanceof DuplicateEntryException) {
            DuplicateEntryException duplicateEntryException = (DuplicateEntryException) ex;
            message = duplicateEntryException.getMessage();
            status = HttpStatus.CONFLICT;
        }
        else if(ex instanceof AuthenticationFailedException) {
            message = ex.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        }
        else if(ex instanceof UserNotFoundException || ex instanceof EventNotFoundException) {
            message = ex.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else if(ex instanceof DuplicateEntryException) {
            DuplicateEntryException duplicateEntryException = (DuplicateEntryException) ex;
            message = duplicateEntryException.getMessage();
            status = HttpStatus.CONFLICT;
        }
        else if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) ex;
            Map<String, String> errors = new HashMap<>();
            methodArgumentNotValidException.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });

            message = objectMapper.writeValueAsString(errors);
            status = HttpStatus.BAD_REQUEST;
        }
        else {
            message = "Error occurred while processing the request";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(new ExceptionResponse(status.value(), message), status);
    }
}