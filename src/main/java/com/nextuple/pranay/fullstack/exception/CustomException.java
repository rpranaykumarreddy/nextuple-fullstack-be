package com.nextuple.pranay.fullstack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomException extends ResponseEntityExceptionHandler {
    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(Exception ex, WebRequest req){
        ErrorDetails errorDetails = new ErrorDetails("Validation Error",ex.getLocalizedMessage(), req.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(Exception ex, WebRequest req){
        ErrorDetails errorDetails = new ErrorDetails("Unauthorized",ex.getLocalizedMessage(), req.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    public static class EntityNotFoundException extends RuntimeException {
        public EntityNotFoundException(String message) {
            super(message);
        }
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(Exception ex, WebRequest req){
        ErrorDetails errorDetails = new ErrorDetails("Entity Not Found",ex.getLocalizedMessage(), req.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    public static class EntityExistsException extends RuntimeException {
        public EntityExistsException(String message) {
            super(message);
        }
    }
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<?> handleEntityExistsException(Exception ex, WebRequest req){
        ErrorDetails errorDetails = new ErrorDetails("Entity Exists",ex.getLocalizedMessage(), req.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }
    public static class UnableToSaveException extends RuntimeException {
        public UnableToSaveException(String message) {
            super(message);
        }
    }
    @ExceptionHandler(UnableToSaveException.class)
    public ResponseEntity<?> handleUnableToSaveException(Exception ex, WebRequest req){
        ErrorDetails errorDetails = new ErrorDetails("Unable to Save",ex.getLocalizedMessage(), req.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
