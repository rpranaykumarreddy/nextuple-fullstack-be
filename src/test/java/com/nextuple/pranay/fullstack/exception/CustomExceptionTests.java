package com.nextuple.pranay.fullstack.exception;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CustomExceptionTests {

    @Mock
    private WebRequest webRequest;

    @InjectMocks
    private CustomException customException;

    @Test
    public void testHandleValidationException() {
        CustomException.ValidationException validationException = new CustomException.ValidationException("Validation error");
        ResponseEntity<?> responseEntity = customException.handleValidationException(validationException, webRequest);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleUnauthorizedException() {
        CustomException.UnauthorizedException unauthorizedException = new CustomException.UnauthorizedException("Unauthorized");
        ResponseEntity<?> responseEntity = customException.handleUnauthorizedException(unauthorizedException, webRequest);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleBadRequestException() {
        CustomException.BadRequestException badRequestException = new CustomException.BadRequestException("Bad request");
        ResponseEntity<?> responseEntity = customException.handleBadRequestException(badRequestException, webRequest);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleEntityNotFoundException() {
        CustomException.EntityNotFoundException entityNotFoundException = new CustomException.EntityNotFoundException("Entity not found");
        ResponseEntity<?> responseEntity = customException.handleEntityNotFoundException(entityNotFoundException, webRequest);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleEntityExistsException() {
        CustomException.EntityExistsException entityExistsException = new CustomException.EntityExistsException("Entity exists");
        ResponseEntity<?> responseEntity = customException.handleEntityExistsException(entityExistsException, webRequest);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleUnableToSaveException() {
        CustomException.UnableToSaveException unableToSaveException = new CustomException.UnableToSaveException("Unable to save");
        ResponseEntity<?> responseEntity = customException.handleUnableToSaveException(unableToSaveException, webRequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}
