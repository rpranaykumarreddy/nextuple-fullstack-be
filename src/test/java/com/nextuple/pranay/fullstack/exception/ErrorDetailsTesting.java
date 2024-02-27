package com.nextuple.pranay.fullstack.exception;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ErrorDetailsTesting {
    @Mock
    private ErrorDetails errorDetails;

    @Test
public void testErrorDetails() {
        errorDetails = new ErrorDetails("error", "message", "description");
        assertEquals("error", errorDetails.getError());
        assertEquals("message", errorDetails.getMessage());
        assertEquals("description", errorDetails.getDescription());
        LocalDateTime errorDetailsTimeStamp = errorDetails.getTimeStamp();
        assertTrue(errorDetailsTimeStamp.isBefore(LocalDateTime.now()) || errorDetailsTimeStamp.isEqual(LocalDateTime.now()));
    }
}
