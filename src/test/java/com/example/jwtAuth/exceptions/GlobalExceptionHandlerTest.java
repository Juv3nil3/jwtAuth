package com.example.jwtAuth.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleValidationExceptions() {
        // Prepare a MethodArgumentNotValidException with validation errors
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        List<FieldError> errors = new ArrayList<>();
        errors.add(new FieldError("jwtRequest", "email", "Invalid email format"));
        errors.add(new FieldError("jwtRequest", "password", "Password must not be empty"));

        when(bindingResult.getFieldErrors()).thenReturn(errors);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        // Call the exception handler method
        List<String> response = globalExceptionHandler.handleValidationExceptions(ex);

        // Verify the response
        List<String> expectedErrors = new ArrayList<>();
        expectedErrors.add("Invalid email format");
        expectedErrors.add("Password must not be empty");
        assertEquals(expectedErrors, response);
    }

    @Test
    void testHandleValidationExceptionsNoErrors() {
        // Prepare a MethodArgumentNotValidException with no validation errors
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.getFieldErrors()).thenReturn(new ArrayList<>());
        when(ex.getBindingResult()).thenReturn(bindingResult);

        // Call the exception handler method
        List<String> response = globalExceptionHandler.handleValidationExceptions(ex);

        // Verify the response (should be an empty list)
        assertEquals(new ArrayList<>(), response);
    }
}